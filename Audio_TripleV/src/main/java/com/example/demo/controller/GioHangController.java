package com.example.demo.controller;

import com.example.demo.entity.CapNhatSoLuongRequest;
import com.example.demo.entity.GioHang;
import com.example.demo.entity.KhachHang;
import com.example.demo.entity.SanPhamChiTiet;
import com.example.demo.service.GioHangService;
import com.example.demo.service.KhachHangService;
import com.example.demo.service.SanPhamChiTietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("/khach-hang/gio-hang")
public class GioHangController {

    @Autowired
    private GioHangService gioHangService;

    @Autowired
    private KhachHangService khachHangService;

    @Autowired
    SanPhamChiTietService sanPhamChiTietService;

    @GetMapping("/hien-thi")
    public String hienThiGioHang(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        KhachHang khachHang = khachHangService.findByTaiKhoan(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Khách hàng không tồn tại"));

        GioHang gioHang = gioHangService.findByKhachHang(khachHang)
                .orElseGet(() -> gioHangService.createGioHang(khachHang));

        double totalPrice = gioHang.getGioHangChiTietList().stream()
                .mapToDouble(item -> item.getSoLuong() * item.getSanPhamChiTiet().getDonGia())
                .sum();

        model.addAttribute("gioHang", gioHang);
        model.addAttribute("totalPrice", totalPrice <= 0 ? totalPrice : 0.0);

        return "customer/gio-hang";
    }


    @PostMapping("/them-san-pham")
    public ResponseEntity<String> themSanPham(@RequestParam Integer sanPhamChiTietId,
                                              @AuthenticationPrincipal UserDetails userDetails,
                                              @RequestParam Integer soLuong) {
        if (sanPhamChiTietId == null || soLuong == null || soLuong <= 0) {
            return ResponseEntity.badRequest().body("ID sản phẩm hoặc số lượng không hợp lệ.");
        }

        System.out.println("sanPhamChiTietId: " + sanPhamChiTietId);  // In ra giá trị để kiểm tra
        System.out.println("soLuong: " + soLuong);  // In ra giá trị số lượng

        // Lấy khách hàng hiện tại từ thông tin đăng nhập
        KhachHang khachHang = khachHangService.findByTaiKhoan(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Khách hàng không tồn tại"));

        // Lấy giỏ hàng của khách hàng
        GioHang gioHang = gioHangService.findByKhachHang(khachHang).orElseGet(() -> gioHangService.createGioHang(khachHang));

        // Lấy sản phẩm chi tiết từ ID
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietService.findById(sanPhamChiTietId);

        // Thêm sản phẩm vào giỏ hàng
        gioHangService.themSanPhamVaoGio(gioHang, sanPhamChiTiet, soLuong);

        return ResponseEntity.ok("Sản phẩm đã được thêm vào giỏ hàng!");
    }


    @PostMapping("/cap-nhat-so-luong")
    @ResponseBody
    public ResponseEntity<?> capNhatSoLuong(@RequestBody CapNhatSoLuongRequest request) {
        try {
            gioHangService.updateQuantity(request.getProductId(), request.getQuantity());
            return ResponseEntity.ok(Map.of("success", true));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }


}
