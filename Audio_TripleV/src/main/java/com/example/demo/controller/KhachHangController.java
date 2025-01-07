package com.example.demo.controller;

import com.example.demo.entity.DonHang;
import com.example.demo.entity.DonHangChiTiet;
import com.example.demo.entity.GioHang;
import com.example.demo.entity.GioHangChiTiet;
import com.example.demo.entity.KhachHang;
import com.example.demo.entity.SanPhamChiTiet;
import com.example.demo.repository.DonHangRepository;
import com.example.demo.repository.SanPhamChiTietRepository;
import com.example.demo.service.*;
import com.example.demo.repository.KhachHangRepository;
import com.example.demo.vnconfig.PaymentInfoDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

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
            if (donHang.getTongGia() != null) {
                String formattedTongGia = currencyFormat.format(donHang.getTongGia());
                donHang.setFormattedTongGia(formattedTongGia);
            } else {
                donHang.setFormattedTongGia("Không có giá"); // Giá trị mặc định khi giá trị là null
            }

            donHang.getDonHangChiTietList().forEach(chiTiet -> {
                if (chiTiet.getDonGia() != null) {
                    String formattedDonGia = currencyFormat.format(chiTiet.getDonGia());
                    chiTiet.setFormattedDonGia(formattedDonGia);
                } else {
                    chiTiet.setFormattedDonGia("Không có giá"); // Giá trị mặc định khi giá trị là null
                }

                // Tính và định dạng 'thanhTien'
                if (chiTiet.getDonGia() != null && chiTiet.getSoLuong() > 0) {
                    double thanhTien = chiTiet.getDonGia() * chiTiet.getSoLuong();
                    String formattedThanhTien = currencyFormat.format(thanhTien);
                    chiTiet.setFormattedThanhTien(formattedThanhTien);
                } else {
                    chiTiet.setFormattedThanhTien("Không có thành tiền"); // Giá trị mặc định cho trường hợp tính toán không hợp lệ
                }
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
    public String hienThiThanhToan(Model model, @RequestParam(required = false) String selectedItems, @AuthenticationPrincipal UserDetails userDetails) {
        // Lấy thông tin khách hàng từ tài khoản đăng nhập
        KhachHang khachHang = khachHangService.findByTaiKhoan(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Khách hàng không tồn tại"));

        // Lấy giỏ hàng của khách hàng
        GioHang gioHang = gioHangService.findByKhachHang(khachHang)
                .orElseThrow(() -> new RuntimeException("Giỏ hàng không tồn tại"));

        // Nếu có các sản phẩm được chọn, lọc giỏ hàng để chỉ hiển thị sản phẩm được chọn
        if (selectedItems != null && !selectedItems.isEmpty()) {
            List<Integer> selectedProductIds = Arrays.stream(selectedItems.split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());

            gioHang.setGioHangChiTietList(gioHang.getGioHangChiTietList().stream()
                    .filter(item -> selectedProductIds.contains(item.getSanPhamChiTiet().getId()))
                    .collect(Collectors.toList()));
        }

        // Tính tổng tiền và số lượng trong giỏ hàng
        double totalPrice = gioHang.getGioHangChiTietList().stream()
                .mapToDouble(item -> item.getSoLuong() * item.getSanPhamChiTiet().getDonGia())
                .sum();
        int totalQuantity = gioHang.getGioHangChiTietList().stream()
                .mapToInt(item -> item.getSoLuong())
                .sum();

        // Định dạng tổng tiền theo tiền tệ Việt Nam
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String formattedTotalPrice = currencyFormat.format(totalPrice);

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

    @PostMapping("/khach-hang/thanh-toan/dat-hang")
    public String datHang(@AuthenticationPrincipal UserDetails userDetails,
                          @RequestParam String fullName,
                          @RequestParam String email,
                          @RequestParam String phone,
                          @RequestParam String address,
                          @RequestParam String paymentMethod,
                          @RequestParam(required = false) String selectedItems,
                          HttpServletRequest request) throws UnsupportedEncodingException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return "redirect:/login"; // Chuyển hướng đến trang đăng nhập nếu người dùng chưa đăng nhập
        }

        if (paymentMethod.equals("card")) {
            KhachHang khachHang = khachHangService.findByTaiKhoan(userDetails.getUsername()).get();
            GioHang gioHang = gioHangService.findByKhachHang(khachHang).get();

            // Truyền danh sách sản phẩm đã chọn vào VNPay
            String url = vnPayService.createOrder(gioHang.getTongGia().intValue(), userDetails.getUsername(), fullName, email, phone, address, request, selectedItems);
            return "redirect:" + url;
        }

        processThanhToan(userDetails.getUsername(), fullName, email, phone, address, selectedItems);
        return "redirect:/khach-hang/don-hang/danh-sach";
    }

    @GetMapping("/khach-hang/thanh-toan/vnpay_return")
    public String vnpayReturn(HttpServletResponse response,@RequestParam(required = false) String selectedItems, @ModelAttribute PaymentInfoDTO paymentInfoDTO) {
        if (paymentInfoDTO.getVnp_OrderInfo() == null || paymentInfoDTO.getVnp_OrderInfo().isEmpty()) {
            throw new RuntimeException("Thông tin thanh toán không hợp lệ");
        }

        String[] userDetail = paymentInfoDTO.getVnp_OrderInfo().split(", ");
        if (userDetail.length < 5) {
            throw new RuntimeException("Dữ liệu không đủ để xử lý thanh toán");
        }

        // Thêm tham số selectedItems nếu cần
        processThanhToan(userDetail[0], userDetail[1], userDetail[2], userDetail[3], userDetail[4], selectedItems);

        return "redirect:/khach-hang/don-hang/danh-sach";
    }

    void processThanhToan(String username, String fullName, String email, String phone, String address, String selectedItems) {
        // Kiểm tra nếu selectedItems là null hoặc rỗng
        if (selectedItems == null || selectedItems.isEmpty()) {
            throw new RuntimeException("Không có sản phẩm nào được chọn.");
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

        // Lấy giỏ hàng của khách hàng
        GioHang gioHang = gioHangService.findByKhachHang(khachHang)
                .orElseThrow(() -> new RuntimeException("Giỏ hàng không tồn tại"));

        // Lọc sản phẩm được chọn
        List<Integer> selectedProductIds = Arrays.stream(selectedItems.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        List<GioHangChiTiet> selectedItemsList = gioHang.getGioHangChiTietList().stream()
                .filter(item -> selectedProductIds.contains(item.getSanPhamChiTiet().getId()))
                .collect(Collectors.toList());

        // Tạo đơn hàng mới
        DonHang donHang = new DonHang();
        donHang.setKhachHang(khachHang);
        donHang.setTongGia(selectedItemsList.stream()
                .mapToDouble(item -> item.getSoLuong() * item.getSanPhamChiTiet().getDonGia())
                .sum());
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
        gioHang.setGioHangChiTietList(gioHang.getGioHangChiTietList().stream()
                .filter(item -> !selectedProductIds.contains(item.getSanPhamChiTiet().getId()))
                .collect(Collectors.toList()));
        gioHangService.save(gioHang);
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
