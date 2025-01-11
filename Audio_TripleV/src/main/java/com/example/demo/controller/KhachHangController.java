package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.service.*;
import com.example.demo.repository.KhachHangRepository;
import com.example.demo.vnconfig.PaymentInfoDTO;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.fasterxml.jackson.core.type.TypeReference;
import org.json.JSONArray;
import org.json.JSONObject;

@Controller
public class KhachHangController {
    @Autowired
    private KhachHangRepository khachHangRepository;

    @Autowired
    private KhachHangService khachHangService;

    @Autowired
    private VNPayService vnPayService;

    @Autowired
    private GioHangService gioHangService;

    @Autowired
    private DonHangService donHangService;

    @Autowired
    private SanPhamChiTietService sanPhamChiTietService;

    private Double totalPrice = 0.0;

    private String savedSelectedItems;


    @GetMapping("/khach-hang/check-login")
    public ResponseEntity<Map<String, Object>> checkLogin(@AuthenticationPrincipal UserDetails userDetails) {
        Map<String, Object> response = new HashMap<>();
        if (userDetails == null) {
            response.put("loggedIn", false);  // Nếu người dùng chưa đăng nhập
        } else {
            response.put("loggedIn", true);  // Nếu người dùng đã đăng nhập
        }
        return ResponseEntity.ok(response);
    }


    private GioHang muaNgayGioHang;

    private Boolean globalMuaNgay = false;

    private Double globalShippingFee = 0.0;

    @GetMapping("/khach-hang/hien-thi")
    public String hienThiDanhSachKhachHang(Model model) {
        List<KhachHang> listKh = khachHangRepository.findAll(); // Lấy danh sách khách hàng
        model.addAttribute("listKh", listKh);
        return "customer/khach-hang";
    }

    // Tìm kiếm khách hàng theo tên và số điện thoại
    @GetMapping("/api/khach-hang/search")
    @ResponseBody
    public List<KhachHang> searchKhachHang(@RequestParam(required = false) String name,
                                           @RequestParam(required = false) String phone) {
        try {
            if ((name == null || name.isEmpty()) && (phone == null || phone.isEmpty())) {
                return Collections.emptyList(); // Trả về danh sách rỗng nếu không có điều kiện tìm kiếm
            }
            return khachHangService.searchByNameAndPhone(name, phone); // Tìm khách hàng
        } catch (Exception e) {
            // log.error("Error in searchKhachHang: ", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi tìm kiếm khách hàng");
        }
    }

    // Lưu khách hàng mới hoặc cập nhật khách hàng
    @PostMapping("/api/khach-hang/save")
    public ResponseEntity<KhachHang> saveKh(@RequestBody KhachHang khachHang) {
        try {
            // Kiểm tra dữ liệu hợp lệ trước khi lưu
            if (khachHang.getTen() == null || khachHang.getSdt() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tên và số điện thoại không thể để trống");
            }
            KhachHang savedKhachHang = khachHangRepository.save(khachHang); // Lưu khách hàng
            return ResponseEntity.ok(savedKhachHang);
        } catch (Exception e) {
            // log.error("Error saving KhachHang: ", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi khi lưu khách hàng");
        }
    }


    @GetMapping("/khach-hang/don-hang/danh-sach")
    public String danhSachDonHang(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        // Lấy khách hàng hiện tại
        KhachHang khachHang = khachHangService.findByTaiKhoan(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Khách hàng không tồn tại"));
        model.addAttribute("khachHang", khachHang);

        // Lấy danh sách đơn hàng
        List<DonHang> donHangList = donHangService.findByKhachHang(khachHang);

        // Định dạng tổng giá của đơn hàng
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        donHangList.forEach(donHang -> {
            // Định dạng tổng giá của đơn hàng
            String formattedTongGia = currencyFormat.format(donHang.getTongGia());
            donHang.setFormattedTongGia(formattedTongGia);

            // Định dạng các chi tiết đơn hàng
            donHang.getDonHangChiTietList().forEach(chiTiet -> {
                String formattedDonGia = currencyFormat.format(chiTiet.getDonGia());
                chiTiet.setFormattedDonGia(formattedDonGia);

                // Tính và định dạng thành tiền
                double thanhTien = chiTiet.getDonGia() * chiTiet.getSoLuong();
                String formattedThanhTien = currencyFormat.format(thanhTien);
                chiTiet.setFormattedThanhTien(formattedThanhTien);
            });
        });

        model.addAttribute("donHangList", donHangList);

        // Lấy giỏ hàng dựa trên khách hàng, nếu chưa có thì tạo mới
        GioHang gioHang = gioHangService.findByKhachHang(khachHang)
                .orElseGet(() -> gioHangService.createGioHang(khachHang));

        // Tính tổng số lượng trong giỏ hàng
        int totalQuantity = 0;
        if (gioHang.getGioHangChiTietList() != null && !gioHang.getGioHangChiTietList().isEmpty()) {
            totalQuantity = gioHang.getGioHangChiTietList().stream()
                    .mapToInt(item -> item.getSoLuong())
                    .sum();
        }
        model.addAttribute("cartCount", totalQuantity);

        return "customer/don-hang-cua-toi"; // Trang hiển thị danh sách đơn hàng
    }

    @GetMapping("/khach-hang/thanh-toan/hien-thi")
    public String hienThiThanhToan(Model model,
                                   @RequestParam(required = false) String selectedItems,
                                   @RequestParam(required = false) Boolean muaNgay,
                                   @AuthenticationPrincipal UserDetails userDetails) throws JsonProcessingException {
        if (selectedItems != null) {
            savedSelectedItems = selectedItems;
        }

        // Lấy thông tin khách hàng từ tài khoản đăng nhập
        KhachHang khachHang = khachHangService.findByTaiKhoan(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Khách hàng không tồn tại"));

        // Lấy giỏ hàng của khách hàng
        GioHang gioHang = gioHangService.findByKhachHang(khachHang)
                .orElseThrow(() -> new RuntimeException("Giỏ hàng không tồn tại"));

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        // Nếu có các sản phẩm được chọn, lọc giỏ hàng và cập nhật số lượng
        if (selectedItems != null && !selectedItems.trim().isEmpty()) {
            try {
                // Parse selectedItems JSON string into a list of ItemDTO
                ObjectMapper objectMapper = new ObjectMapper();
                List<ItemDTO> selectedItemList = objectMapper.readValue(selectedItems, new TypeReference<List<ItemDTO>>() {});

                // Lọc giỏ hàng theo danh sách sản phẩm được chọn và cập nhật số lượng
                Map<Integer, Integer> selectedItemMap = selectedItemList.stream()
                        .collect(Collectors.toMap(ItemDTO::getId, ItemDTO::getQuantity));

                gioHang.getGioHangChiTietList().forEach(item ->
                        System.out.println("Cart Item ID: " + item.getSanPhamChiTiet().getId() + ", Quantity: " + item.getSoLuong())
                );

                if(gioHang.getGioHangChiTietList().size() > 0) {
                    System.out.println("length  " + gioHang.getGioHangChiTietList().size());
                }

                // First, filter the items based on whether their ID is in the selectedItemMap
                List<GioHangChiTiet> filteredItems = gioHang.getGioHangChiTietList().stream()
                        // Log before filtering to check which items are being considered

                        // Filter only items whose ID is in the selected items map
                        .filter(item -> selectedItemMap.containsKey(item.getSanPhamChiTiet().getId()))
                        .collect(Collectors.toList());

                // Now, update the quantities for the filtered items
                List<GioHangChiTiet> updatedItems = filteredItems.stream()
                        // Update the quantity for the matching items
                        .peek(item -> {
                            Integer quantity = selectedItemMap.get(item.getSanPhamChiTiet().getId());
                            if (quantity != null) {
                                item.setSoLuong(quantity); // Set the updated quantity
                                System.out.println("Updated Quantity for Item ID: " + item.getSanPhamChiTiet().getId() + " | New Quantity: " + quantity);
                            }
                        })
                        .collect(Collectors.toList());

                // Assign the updated items back to the cart
                gioHang.setGioHangChiTietList(updatedItems);

                // Log the updated cart items after processing
                gioHang.getGioHangChiTietList().forEach(item ->
                        System.out.println("Updated Cart Item ID: " + item.getSanPhamChiTiet().getId() + ", Quantity: " + item.getSoLuong())
                );


            } catch (Exception e) {
                throw new RuntimeException("Định dạng dữ liệu đầu vào không hợp lệ", e);
            }
        }

        // Tính tổng tiền và số lượng trong giỏ hàng
        totalPrice = gioHang.getGioHangChiTietList().stream()
                .mapToDouble(item -> item.getSoLuong() * item.getSanPhamChiTiet().getDonGia())
                .sum();
        int totalQuantity = gioHang.getGioHangChiTietList().stream()
                .mapToInt(GioHangChiTiet::getSoLuong)
                .sum();

        // Định dạng tổng tiền theo tiền tệ Việt Nam
        String formattedTotalPrice = currencyFormat.format(totalPrice);

        // Định dạng từng sản phẩm trong giỏ hàng
        gioHang.getGioHangChiTietList().forEach(item -> {
            String formattedDonGia = item.getSanPhamChiTiet().getFormattedDonGia();
            item.getSanPhamChiTiet().setFormattedDonGia(formattedDonGia);
        });
//
//        if (muaNgay != null && muaNgay) {
//            // Handle "Mua ngay" case separately, e.g., skip loading full cart
//          gioHang.setGioHangChiTietList(updatedItems);
//        }

        model.addAttribute("gioHang", gioHang);

        // Thêm dữ liệu vào model
        model.addAttribute("cartCount", totalQuantity);
        model.addAttribute("totalPrice", formattedTotalPrice);  // Truyền tổng tiền đã định dạng



        if (Boolean.TRUE.equals(muaNgay)) {
            ObjectMapper objectMapper = new ObjectMapper();
            List<ItemDTO> selectedItemList = objectMapper.readValue(selectedItems, new TypeReference<List<ItemDTO>>() {});

            // Lọc giỏ hàng theo danh sách sản phẩm được chọn và cập nhật số lượng
            Map<Integer, Integer> selectedItemMap = selectedItemList.stream()
                    .collect(Collectors.toMap(ItemDTO::getId, ItemDTO::getQuantity));


            // "Mua ngay" case: create a temporary cart with only the selected item
            List<GioHangChiTiet> temporaryItems = selectedItemList.stream()
                    .map(item -> createGioHangChiTiet(item, selectedItemMap.get(item.getId())))
                    .collect(Collectors.toList());
            gioHang.setGioHangChiTietList(temporaryItems);


            model.addAttribute("gioHang", gioHang);
            totalPrice = gioHang.getGioHangChiTietList().stream()
                    .mapToDouble(item -> item.getSoLuong() * item.getSanPhamChiTiet().getDonGia())
                    .sum();

            totalQuantity = gioHang.getGioHangChiTietList().stream()
                    .mapToInt(GioHangChiTiet::getSoLuong)
                    .sum();

            // Format total price as Vietnamese currency
            formattedTotalPrice = currencyFormat.format(totalPrice);

            muaNgayGioHang = gioHang;
            globalMuaNgay = true;

            // Add the cart details and the formatted total price to the model
            model.addAttribute("gioHang", gioHang);
            model.addAttribute("cartCount", totalQuantity);
            model.addAttribute("totalPrice", formattedTotalPrice);  // Truyền tổng tiền đã định dạng

        }
        // Định dạng từng sản phẩm trong giỏ hàng
        gioHang.getGioHangChiTietList().forEach(item -> {
            item.setFormattedDonGia(currencyFormat.format(item.getSanPhamChiTiet().getDonGia()));
            item.setFormattedTongGia(currencyFormat.format(item.getTongGia()));
        });

        // Thêm dữ liệu vào model
        model.addAttribute("cartCount", totalQuantity);
        model.addAttribute("khachHang", khachHang);
        model.addAttribute("gioHang", gioHang);
        model.addAttribute("formattedTotalPrice", formattedTotalPrice);  // Truyền tổng tiền đã định dạng
        model.addAttribute("totalPrice", totalPrice);

        return "customer/thanh-toan"; // Tên file HTML trong thư mục template
    }

    public GioHangChiTiet createGioHangChiTiet(ItemDTO itemDTO, Integer quantity) {
        // Create a new GioHangChiTiet object for each item in the selected list
        GioHangChiTiet gioHangChiTiet = new GioHangChiTiet();

        // Find SanPhamChiTiet using the item ID
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietService.findSanPhamChiTietById(itemDTO.getId());

        // Set the product details and quantity
        gioHangChiTiet.setSanPhamChiTiet(sanPhamChiTiet);
        gioHangChiTiet.setSoLuong(quantity);

        // Optionally, you can associate the GioHangChiTiet with a cart if needed
        // gioHangChiTiet.setGioHang(gioHang);

        return gioHangChiTiet;
    }

    @PostMapping("/khach-hang/thanh-toan/dat-hang")
    public String datHang(@AuthenticationPrincipal UserDetails userDetails,
                          @RequestParam String fullName,
                          @RequestParam String email,
                          @RequestParam String phone,
                          @RequestParam String address,
                          @RequestParam String paymentMethod,
                          @RequestParam(required = false) String shippingFee, // Added shippingFee parameter
                          @RequestParam(required = false) String selectedItems,
                          HttpServletRequest request) throws UnsupportedEncodingException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return "redirect:/login"; // Chuyển hướng đến trang đăng nhập nếu người dùng chưa đăng nhập
        }

        globalShippingFee = (shippingFee != null) ? Double.parseDouble(shippingFee) : 0.0;

        // Log or use the shippingFeeValue as needed
        System.out.println("Shipping Fee: " + globalShippingFee);


//        if (paymentMethod.equals("card")) {
//            KhachHang khachHang = khachHangService.findByTaiKhoan(userDetails.getUsername()).get();
//            GioHang gioHang = gioHangService.findByKhachHang(khachHang).get();
//
//            String url = vnPayService.createOrder(gioHang.getTongGia().intValue(), userDetails.getUsername(), fullName, email, phone, address, request);
//            return "redirect:" + url;
//        }
        if (paymentMethod.equals("card")) {
            KhachHang khachHang = khachHangService.findByTaiKhoan(userDetails.getUsername()).get();
            GioHang gioHang = gioHangService.findByKhachHang(khachHang).get();

            // Truyền danh sách sản phẩm đã chọn vào VNPay
            String url = vnPayService.createOrder(totalPrice.intValue() + globalShippingFee.intValue(), userDetails.getUsername(), fullName, email, phone, address, request, selectedItems);
            return "redirect:" + url;
        }


        processThanhToan(userDetails.getUsername(), fullName, email, phone, address, savedSelectedItems);
        return "redirect:/khach-hang/don-hang/danh-sach";
    }

    @GetMapping("/khach-hang/thanh-toan/vnpay_return")
    public String vnpayReturn(HttpServletResponse response, @RequestParam(required = false) String selectedItems, @ModelAttribute PaymentInfoDTO paymentInfoDTO, HttpServletRequest request) {
        int status = vnPayService.orderReturn(request);
        if(status == 0) {

            // failure
            return "redirect:/khach-hang/don-hang/danh-sach";
        }
        else {
            // sucess
            if (paymentInfoDTO.getVnp_OrderInfo() == null || paymentInfoDTO.getVnp_OrderInfo().isEmpty()) {
                throw new RuntimeException("Thông tin thanh toán không hợp lệ");
            }


            String[] userDetail = paymentInfoDTO.getVnp_OrderInfo().split(", ");
            if (userDetail.length < 5) {
                throw new RuntimeException("Dữ liệu không đủ để xử lý thanh toán");
            }

            // Thêm tham số selectedItems nếu cần
            processThanhToan(userDetail[0], userDetail[1], userDetail[2], userDetail[3], userDetail[4], savedSelectedItems);

            return "redirect:/khach-hang/don-hang/danh-sach";
        }

    }

    void processThanhToan(String username, String fullName, String email, String phone, String address, String selectedItems) {
        // Kiểm tra nếu selectedItems là null hoặc rỗng
        if (selectedItems == null || selectedItems.isEmpty()) {
            throw new RuntimeException("Không có sản phẩm nào được chọn.");

        }

        else {
            System.out.println("list not empty ");
        }
        // Lấy khách hàng hiện tại
        KhachHang khachHang = khachHangService.findByTaiKhoan(username)
                .orElseThrow(() -> new RuntimeException("Khách hàng không tồn tại"));

        // Cập nhật thông tin khách hàng nếu có thay đổi
        if (!fullName.contains("?")) {
            khachHang.setTen(fullName);
        }
        khachHang.setEmail(email);
        khachHang.setSdt(phone);
        khachHang.setDiaChi(address);

        // Lưu lại thông tin khách hàng đã cập nhật
        khachHangService.save(khachHang);


        JSONArray jsonArray = new JSONArray(selectedItems);

        // Extract only the IDs using a stream
        List<Integer> selectedProductIds = IntStream.range(0, jsonArray.length())
                .mapToObj(i -> jsonArray.getJSONObject(i).getInt("id"))
                .collect(Collectors.toList());

        List<GioHangChiTiet> selectedItemsList = null; GioHang gioHang = null;

        if (!globalMuaNgay) {
            // Lấy giỏ hàng của khách hàng
            gioHang = gioHangService.findByKhachHang(khachHang)
                    .orElseThrow(() -> new RuntimeException("Giỏ hàng không tồn tại"));

            // Lọc sản phẩm được chọn
//        List<Integer> selectedProductIds = Arrays.stream(selectedItems.split(","))
//                .map(Integer::parseInt)
//                .collect(Collectors.toList());
//
//            JSONArray jsonArray = new JSONArray(selectedItems);
//
//            // Extract only the IDs using a stream
//            List<Integer> selectedProductIds = IntStream.range(0, jsonArray.length())
//                    .mapToObj(i -> jsonArray.getJSONObject(i).getInt("id"))
//                    .collect(Collectors.toList());


            selectedItemsList = gioHang.getGioHangChiTietList().stream()
                    .filter(item -> selectedProductIds.contains(item.getSanPhamChiTiet().getId()))
                    .collect(Collectors.toList());

        } else {

            // mua ngay true
            selectedItemsList = muaNgayGioHang.getGioHangChiTietList().stream()
                    .filter(item -> selectedProductIds.contains(item.getSanPhamChiTiet().getId()))
                    .collect(Collectors.toList());

        }
        // Tạo đơn hàng mới
        DonHang donHang = new DonHang();
        donHang.setKhachHang(khachHang);
        donHang.setTongGia(selectedItemsList.stream()
                .mapToDouble(item -> item.getSoLuong() * item.getSanPhamChiTiet().getDonGia())
                .sum() +  globalShippingFee);
        donHang.setTrangThai("Chờ xử lý");
        donHang.setNgayTao(new Date());

        // Chuyển các sản phẩm đã chọn từ giỏ hàng sang chi tiết đơn hàng và cập nhật số lượng
        List<DonHangChiTiet> chiTietList = selectedItemsList.stream()
                .map(item -> {
                    // Giảm số lượng sản phẩm chi tiết
                    SanPhamChiTiet sanPhamChiTiet = item.getSanPhamChiTiet();
                    int soLuongTrongKho = sanPhamChiTiet.getSoLuong();
                    int soLuongDatMua = item.getSoLuong();

                    if (soLuongDatMua > soLuongTrongKho) {
                        throw new RuntimeException("Sản phẩm " + sanPhamChiTiet.getSanPham().getTen() + " không đủ số lượng trong kho.");
                    }

                    sanPhamChiTiet.setSoLuong(soLuongTrongKho - soLuongDatMua);
                    sanPhamChiTietService.addSanPhamChiTiet(sanPhamChiTiet);

                    // Tạo chi tiết đơn hàng
                    DonHangChiTiet chiTiet = new DonHangChiTiet();
                    chiTiet.setSanPhamChiTiet(sanPhamChiTiet);
                    chiTiet.setSoLuong(soLuongDatMua);
                    chiTiet.setDonGia(sanPhamChiTiet.getDonGia());
                    chiTiet.setDonHang(donHang);
                    chiTiet.setNgayTao(new Date());
                    return chiTiet;
                })
                .collect(Collectors.toList());

        donHang.setDonHangChiTietList(chiTietList);

        // Lưu đơn hàng vào cơ sở dữ liệu
        donHangService.save(donHang);

        // Xóa các sản phẩm đã chọn khỏi giỏ hàng
        if(!globalMuaNgay) {
            gioHang.setGioHangChiTietList(gioHang.getGioHangChiTietList().stream()
                    .filter(item -> !selectedProductIds.contains(item.getSanPhamChiTiet().getId()))
                    .collect(Collectors.toList()));
            gioHangService.save(gioHang);
        }

        if(globalMuaNgay) globalMuaNgay = false;
    }


    @GetMapping("/khach-hang/thong-tin")
    public String thongTinTaiKhoan(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        KhachHang khachHang = khachHangService.findByTaiKhoan(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Khách hàng không tồn tại"));
        // Lấy giỏ hàng của khách hàng
        GioHang gioHang = gioHangService.findByKhachHang(khachHang)
                .orElseThrow(() -> new RuntimeException("Giỏ hàng không tồn tại"));
        // Tính tổng số lượng trong giỏ hàng
        int totalQuantity = 0; // For cart count
        if (gioHang.getGioHangChiTietList() != null && !gioHang.getGioHangChiTietList().isEmpty()) {
            totalQuantity = gioHang.getGioHangChiTietList().stream()
                    .mapToInt(item -> item.getSoLuong())
                    .sum();
        }
        model.addAttribute("cartCount", totalQuantity);
        model.addAttribute("khachHang", khachHang);
        return "customer/tai-khoan";
    }

    @PostMapping("/khach-hang/thong-tin/cap-nhat")
    public String capNhatThongTinTaiKhoan(@AuthenticationPrincipal UserDetails userDetails,
                                          @ModelAttribute KhachHang khachHangCapNhat,
                                          RedirectAttributes redirectAttributes) {
        // Lấy khách hàng hiện tại từ cơ sở dữ liệu
        KhachHang khachHang = khachHangService.findByTaiKhoan(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Khách hàng không tồn tại"));

        // Cập nhật thông tin
        khachHang.setTen(khachHangCapNhat.getTen());
        khachHang.setEmail(khachHangCapNhat.getEmail());
        khachHang.setSdt(khachHangCapNhat.getSdt());
        khachHang.setDiaChi(khachHangCapNhat.getDiaChi());

        // Lưu lại thông tin cập nhật
        khachHangService.save(khachHang);

        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thông tin thành công!");
        return "redirect:/khach-hang/thong-tin";
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/khach-hang/thong-tin/doi-mat-khau")
    public String doiMatKhau(@AuthenticationPrincipal UserDetails userDetails,
                             @RequestParam("matKhauCu") String matKhauCu,
                             @RequestParam("matKhauMoi") String matKhauMoi,
                             @RequestParam("xacNhanMatKhauMoi") String xacNhanMatKhauMoi,
                             RedirectAttributes redirectAttributes) {
        // Lấy thông tin khách hàng hiện tại
        KhachHang khachHang = khachHangService.findByTaiKhoan(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Khách hàng không tồn tại"));

        // Kiểm tra mật khẩu cũ
        if (!passwordEncoder.matches(matKhauCu, khachHang.getMatKhau())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Mật khẩu cũ không đúng!");
            return "redirect:/khach-hang/thong-tin";
        }

        // Kiểm tra mật khẩu mới có khớp với xác nhận mật khẩu không
        if (!matKhauMoi.equals(xacNhanMatKhauMoi)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Mật khẩu mới và xác nhận mật khẩu không khớp!");
            return "redirect:/khach-hang/thong-tin";
        }

        // Đổi mật khẩu
        khachHang.setMatKhau(passwordEncoder.encode(matKhauMoi));
        khachHangService.save(khachHang);

        redirectAttributes.addFlashAttribute("successMessage", "Đổi mật khẩu thành công!");
        return "redirect:/khach-hang/thong-tin";
    }

    @PostMapping("/khach-hang/don-hang/huy/{id}")
    public String huyDonHang(@PathVariable("id") Integer id) {
        // Logic hủy đơn hàng
        donHangService.huyDonHang(id); // Giả sử bạn có service xử lý hủy đơn hàng
        return "redirect:/khach-hang/don-hang/danh-sach"; // Quay lại danh sách đơn hàng
    }
}
