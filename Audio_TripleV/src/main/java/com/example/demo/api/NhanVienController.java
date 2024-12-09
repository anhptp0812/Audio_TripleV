package com.example.demo.api;

import com.example.demo.entity.*;
import com.example.demo.repository.HangRepository;
import com.example.demo.repository.HoaDonRepository;
import com.example.demo.repository.KhachHangRepository;
import com.example.demo.repository.LoaiSanPhamRepository;
import com.example.demo.repository.MauSacRepository;
import com.example.demo.repository.SanPhamChiTietRepository;
import com.example.demo.repository.SanPhamRepository;
import com.example.demo.service.HoaDonService;
import com.example.demo.service.KhachHangService;
import com.example.demo.service.NhanVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
//@RestController
public class NhanVienController {

    @Autowired
    private NhanVienService nhanVienService;

    @GetMapping("/user/thong-tin")
    public String thongTinTaiKhoanNV(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        // Lấy khách hàng hiện tại
        NhanVien nhanVien = nhanVienService.findByTaiKhoan(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Nhân Viên không tồn tại"));
        model.addAttribute("nhanVien", nhanVien);

//        // Lấy danh sách đơn hàng
//        List<DonHang> donHangList = donHangService.findByKhachHang(khachHang);
//
//        model.addAttribute("donHangList", donHangList);
//        model.addAttribute("counter", 0);
//
//        // Lấy giỏ hàng dựa trên khách hàng, nếu chưa có thì tạo mới
//        GioHang gioHang = gioHangService.findByKhachHang(khachHang)
//                .orElseGet(() -> gioHangService.createGioHang(khachHang));
//
//        // Tính tổng số lượng trong giỏ hàng
//        int totalQuantity = 0; // For cart count
//        if (gioHang.getGioHangChiTietList() != null && !gioHang.getGioHangChiTietList().isEmpty()) {
//            totalQuantity = gioHang.getGioHangChiTietList().stream()
//                    .mapToInt(item -> item.getSoLuong())
//                    .sum();
//        }
//        model.addAttribute("cartCount", totalQuantity);khoan

        return "nhanvien/tai-khoan-user"; // Trang hiển thị danh sách đơn hàng
    }

}
