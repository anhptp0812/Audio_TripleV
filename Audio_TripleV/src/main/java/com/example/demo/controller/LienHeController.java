package com.example.demo.controller;

import com.example.demo.entity.GioHang;
import com.example.demo.entity.KhachHang;
import com.example.demo.entity.SanPhamChiTiet;
import com.example.demo.service.GioHangService;
import com.example.demo.service.KhachHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.NumberFormat;
import java.util.Locale;

@Controller
@RequestMapping("/khach-hang")
public class LienHeController {

    @Autowired
    KhachHangService khachHangService;

    @Autowired
    GioHangService gioHangService;
    @GetMapping("/lien-he/hien-thi")
    public String getLienHe(@AuthenticationPrincipal UserDetails userDetails,
                                    Model model) {
        // Lấy thông tin khách hàng
        if (userDetails != null) {
            KhachHang khachHang = khachHangService.findByTaiKhoan(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("Khách hàng không tồn tại"));
            // Lấy tên khách hàng
            model.addAttribute("fullName", khachHang.getTen());


            // Lấy giỏ hàng, nếu đã đăng nhập sẽ tìm theo khách hàng, nếu chưa thì tạo giỏ hàng trống
            GioHang gioHang = (khachHang != null) ? gioHangService.findByKhachHang(khachHang)
                    .orElseGet(() -> new GioHang()) : new GioHang(); // Tạo giỏ hàng trống nếu không có khách hàng

            // Tính tổng số lượng sản phẩm trong giỏ hàng
            int totalQuantity = gioHang.getGioHangChiTietList().stream()
                    .mapToInt(item -> item.getSoLuong())
                    .sum();
            model.addAttribute("cartCount", totalQuantity);

        }else {
            // Nếu không đăng nhập, gán giá trị mặc định
            model.addAttribute("fullName", "Khách");
            model.addAttribute("cartCount", 0);
        }

        return "customer/lien-he";
    }

}
