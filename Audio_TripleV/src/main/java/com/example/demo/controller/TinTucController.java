package com.example.demo.controller;

import com.example.demo.entity.GioHang;
import com.example.demo.entity.KhachHang;
import com.example.demo.entity.SanPhamChiTiet;
import com.example.demo.service.GioHangService;
import com.example.demo.service.KhachHangService;
import com.example.demo.service.SanPhamChiTietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("khach-hang")
public class TinTucController {
    @Autowired
    SanPhamChiTietService sanPhamChiTietService;

    @Autowired
    KhachHangService khachHangService;

    @Autowired
    GioHangService gioHangService;

    @GetMapping("/trang-chu/hien-thi")
    public String showTop4SanPhamMoiAndSanPhamXemNhieuNhat(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        KhachHang khachHang = khachHangService.findByTaiKhoan(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Khách hàng không tồn tại"));
        model.addAttribute("fullName", khachHang.getTen());

        // Lấy giỏ hàng dựa trên khách hàng, nếu chưa có thì tạo mới
        GioHang gioHang = gioHangService.findByKhachHang(khachHang)
                .orElseGet(() -> gioHangService.createGioHang(khachHang));

        // Tính tổng số lượng trong giỏ hàng
        int totalQuantity = 0; // For cart count
        if (gioHang.getGioHangChiTietList() != null && !gioHang.getGioHangChiTietList().isEmpty()) {
            totalQuantity = gioHang.getGioHangChiTietList().stream()
                    .mapToInt(item -> item.getSoLuong())
                    .sum();
        }
        model.addAttribute("cartCount", totalQuantity);

        List<SanPhamChiTiet> newestProducts = sanPhamChiTietService.getTop4NewestProducts();
        model.addAttribute("sanPhamChiTietList", newestProducts);
        List<SanPhamChiTiet> sanPhamChiTietList = sanPhamChiTietService.getTop4SanPhamChiTiet();
        model.addAttribute("sanPhamChiTietList1", sanPhamChiTietList);
        return "customer/tin-tuc";
    }

    @GetMapping("/san-pham/hien-thi/{id}")
    public String hienThiSanPham(Model model , @PathVariable("id") Integer id ) {
        model.addAttribute("spct", sanPhamChiTietService.findById(id));
        return "customer/san-pham-chi-tiet-khach-hang";
    }


}
