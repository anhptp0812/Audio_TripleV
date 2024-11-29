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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.HashMap;
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
        // Lấy thông tin khách hàng từ tài khoản đăng nhập
        KhachHang khachHang = khachHangService.findByTaiKhoan(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Khách hàng không tồn tại"));
        model.addAttribute("fullName", khachHang.getTen());

        // Lấy giỏ hàng dựa trên khách hàng, nếu chưa có thì tạo mới
        GioHang gioHang = gioHangService.findByKhachHang(khachHang)
                .orElseGet(() -> gioHangService.createGioHang(khachHang));

        // Tính tổng tiền
        double totalPrice = 0.0;
        if (gioHang.getGioHangChiTietList() != null && !gioHang.getGioHangChiTietList().isEmpty()) {
            totalPrice = gioHang.getGioHangChiTietList().stream()
                    .mapToDouble(item -> item.getSoLuong() * item.getSanPhamChiTiet().getDonGia())
                    .sum();
        }
        int totalQuantity = gioHang.getGioHangChiTietList().stream()
                .mapToInt(item -> item.getSoLuong())
                .sum();

        // Truyền thông tin giỏ hàng và tổng tiền vào model
        model.addAttribute("gioHang", gioHang);
        model.addAttribute("totalPrice", totalPrice);

        return "customer/gio-hang"; // Tên file HTML trong thư mục template
    }

    @PostMapping("/them-san-pham")
    public ResponseEntity<Map<String, Object>> themSanPham(@RequestParam Integer sanPhamChiTietId,
                                                           @AuthenticationPrincipal UserDetails userDetails,
                                                           @RequestParam Integer soLuong) {
        if (sanPhamChiTietId == null || soLuong == null || soLuong <= 0) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "ID sản phẩm hoặc số lượng không hợp lệ."));
        }

        // Lấy khách hàng hiện tại từ thông tin đăng nhập
        KhachHang khachHang = khachHangService.findByTaiKhoan(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Khách hàng không tồn tại"));

        // Lấy giỏ hàng của khách hàng
        GioHang gioHang = gioHangService.findByKhachHang(khachHang).orElseGet(() -> gioHangService.createGioHang(khachHang));

        // Lấy sản phẩm chi tiết từ ID
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietService.findById(sanPhamChiTietId);

        // Kiểm tra nếu số lượng sản phẩm trong kho không đủ
        if (sanPhamChiTiet.getSoLuong() == 0) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Sản phẩm đã hết hàng."));
        }

        // Kiểm tra sản phẩm đã có trong giỏ hàng chưa
        boolean exists = gioHang.getGioHangChiTietList().stream()
                .anyMatch(item -> item.getSanPhamChiTiet().getId().equals(sanPhamChiTietId));

        if (exists) {
            // Nếu sản phẩm đã có trong giỏ hàng, kiểm tra xem có đủ số lượng không
            if (sanPhamChiTiet.getSoLuong() < soLuong) {
                return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Không đủ số lượng sản phẩm trong kho."));
            }
            // Cập nhật số lượng sản phẩm trong giỏ hàng
            return ResponseEntity.ok(Collections.singletonMap("message", "Sản phẩm đã có trong giỏ hàng!"));
        } else {
            // Nếu sản phẩm chưa có trong giỏ hàng, kiểm tra xem số lượng có đủ không
            if (sanPhamChiTiet.getSoLuong() < soLuong) {
                return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Không đủ số lượng sản phẩm trong kho."));
            }
            // Thêm sản phẩm vào giỏ hàng nếu số lượng đủ
            gioHangService.themSanPhamVaoGio(gioHang, sanPhamChiTiet, soLuong);
        }

        // Tính lại số lượng sản phẩm trong giỏ hàng
        Integer cartCount = gioHang.getGioHangChiTietList().stream()
                .mapToInt(item -> item.getSoLuong())
                .sum();

        // Trả về số lượng sản phẩm trong giỏ hàng và thông báo
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Sản phẩm đã được thêm vào giỏ hàng!");
        response.put("cartCount", cartCount);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/cap-nhat-so-luong")
    @ResponseBody
    public ResponseEntity<?> capNhatSoLuong(@RequestBody CapNhatSoLuongRequest request) {
        try {
            // Kiểm tra số lượng tồn kho
            SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietService.findById(request.getProductId());
            if (sanPhamChiTiet.getSoLuong() < request.getQuantity()) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Không đủ số lượng sản phẩm trong kho."));
            }

            // Cập nhật số lượng trong giỏ hàng
            gioHangService.updateQuantity(request.getProductId(), request.getQuantity());
            return ResponseEntity.ok(Map.of("success", true));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @GetMapping("/xoa/{sanPhamChiTietId}")
    public ResponseEntity<?> xoaSanPhamKhoiGioHang(@PathVariable Integer sanPhamChiTietId,
                                                   @AuthenticationPrincipal UserDetails userDetails) {
        // Lấy thông tin khách hàng từ userDetails
        KhachHang khachHang = khachHangService.findByTaiKhoan(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Khách hàng không tồn tại"));

        // Xóa sản phẩm khỏi giỏ hàng
        gioHangService.removeSanPham(khachHang, sanPhamChiTietId);

        // Cập nhật tổng giá sau khi xóa sản phẩm
        GioHang gioHang = gioHangService.findByKhachHang(khachHang)
                .orElseThrow(() -> new RuntimeException("Giỏ hàng không tồn tại"));

        // Kiểm tra nếu giỏ hàng không còn sản phẩm nào
        if (gioHang.getGioHangChiTietList().isEmpty()) {
            gioHang.setTongGia(0.0);
        } else {
            gioHangService.updateTongGia(gioHang);
        }

        // Trả về ResponseEntity với mã trạng thái 200 OK và thông báo
        return ResponseEntity.ok().body("Sản phẩm đã được xóa khỏi giỏ hàng.");
    }

}
