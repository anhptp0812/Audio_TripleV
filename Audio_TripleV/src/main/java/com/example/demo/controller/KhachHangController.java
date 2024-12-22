package com.example.demo.controller;

import com.example.demo.entity.DonHang;
import com.example.demo.entity.DonHangChiTiet;
import com.example.demo.entity.GioHang;
import com.example.demo.entity.KhachHang;
import com.example.demo.entity.SanPhamChiTiet;
import com.example.demo.service.*;
import com.example.demo.repository.KhachHangRepository;
import com.example.demo.vnconfig.PaymentInfoDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Controller
public class KhachHangController {
    @Autowired
    private KhachHangRepository khachHangRepository;

    @Autowired
    private KhachHangService khachHangService;

    @Autowired
    private VNPayService vnPayService;

    @GetMapping("hien-thi")
    public List<KhachHang> hienThiKhachHang() {

        return khachHangRepository.findAll();
    }

    @GetMapping("/api/khach-hang/search")
    @ResponseBody
    public List<KhachHang> searchKhachHang(@RequestParam(required = false) String name,
                                           @RequestParam(required = false) String phone) {
        // Nếu cả hai đều null, trả về danh sách rỗng
        if (name == null && phone == null) {
            return Collections.emptyList();
        }
        return khachHangService.searchByNameAndPhone(name, phone); // Tìm kiếm khách hàng theo tên hoặc số điện thoại
    }

    @PostMapping("/api/khach-hang/save")
    public ResponseEntity<KhachHang> saveKh(@RequestBody KhachHang khachHang) {
        KhachHang savedKhachHang = khachHangRepository.save(khachHang);
        return ResponseEntity.ok(savedKhachHang);
    }

    @Autowired
    private GioHangService gioHangService;

    @Autowired
    private DonHangService donHangService;

    @Autowired
    private SanPhamChiTietService sanPhamChiTietService;

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
            String formattedDonGia = item.getSanPhamChiTiet().getFormattedDonGia();
            item.getSanPhamChiTiet().setFormattedDonGia(formattedDonGia);
        });

        // Thêm dữ liệu vào model
        model.addAttribute("cartCount", totalQuantity);
        model.addAttribute("khachHang", khachHang);
        model.addAttribute("gioHang", gioHang);
        model.addAttribute("totalPrice", formattedTotalPrice);  // Truyền tổng tiền đã định dạng

        return "customer/thanh-toan"; // Tên file HTML trong thư mục template
    }

    @PostMapping("/khach-hang/thanh-toan/dat-hang")
    public String datHang(@AuthenticationPrincipal UserDetails userDetails,
                          @RequestParam String fullName,
                          @RequestParam String email,
                          @RequestParam String phone,
                          @RequestParam String address,
                          @RequestParam String paymentMethod,
                          HttpServletRequest request) throws UnsupportedEncodingException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return "redirect:/login"; // Chuyển hướng đến trang đăng nhập nếu người dùng chưa đăng nhập
        }

        if(paymentMethod.equals("card")) {
            KhachHang khachHang = khachHangService.findByTaiKhoan(userDetails.getUsername()).get();
            GioHang gioHang = gioHangService.findByKhachHang(khachHang).get();

            String url = vnPayService.createOrder(gioHang.getTongGia().intValue(), userDetails.getUsername(), fullName, email, phone, address, request);
            return "redirect:" + url;
        }
        processThanhToan(userDetails.getUsername(), fullName, email, phone, address);
        return "redirect:/khach-hang/don-hang/danh-sach";
    }


    @GetMapping("/khach-hang/thanh-toan/vnpay_return")
    public String vnpayReturn(HttpServletResponse response, @ModelAttribute PaymentInfoDTO paymentInfoDTO){
        String[] userDetail = paymentInfoDTO.getVnp_OrderInfo().split(", ");
        processThanhToan(userDetail[0], userDetail[1], userDetail[2], userDetail[3], userDetail[4]);
        return "redirect:/khach-hang/don-hang/danh-sach";
    }
    void processThanhToan( String username, String fullName, String email, String phone, String address){
        // Lấy khách hàng hiện tại
        KhachHang khachHang = khachHangService.findByTaiKhoan(username)
                .orElseThrow(() -> new RuntimeException("Khách hàng không tồn tại"));

        // Cập nhật thông tin khách hàng nếu có thay đổi
        if(!fullName.contains("?")){
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

        // Tạo đơn hàng mới
        DonHang donHang = new DonHang();
        donHang.setKhachHang(khachHang);
        donHang.setTongGia(gioHang.getTongGia());
        donHang.setTrangThai("Chờ xử lý");
        donHang.setNgayTao(new Date());

        // Chuyển các sản phẩm từ giỏ hàng sang chi tiết đơn hàng và cập nhật số lượng
        List<DonHangChiTiet> chiTietList = gioHang.getGioHangChiTietList().stream()
                .map(item -> {
                    // Giảm số lượng sản phẩm chi tiết
                    SanPhamChiTiet sanPhamChiTiet = item.getSanPhamChiTiet();
                    int soLuongTrongKho = sanPhamChiTiet.getSoLuong();
                    int soLuongDatMua = item.getSoLuong();

                    if (soLuongDatMua > soLuongTrongKho) {
                        throw new RuntimeException("Sản phẩm " + sanPhamChiTiet.getSanPham().getTen() + " không đủ số lượng trong kho.");
                    }

                    sanPhamChiTiet.setSoLuong(soLuongTrongKho - soLuongDatMua);
                    sanPhamChiTietService.addSanPhamChiTiet(sanPhamChiTiet); // Cập nhật số lượng sản phẩm chi tiết trong DB

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

        // Xóa giỏ hàng sau khi tạo đơn hàng
        gioHangService.clearGioHang(gioHang);
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
