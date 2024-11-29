package com.example.demo.controller;

import com.example.demo.entity.GioHang;
import com.example.demo.entity.KhachHang;
import com.example.demo.entity.SanPhamChiTiet;
import com.example.demo.repository.SanPhamRepository;
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
@RequestMapping("/khach-hang")
public class SanPhamController {

    @Autowired
    private SanPhamChiTietService sanPhamChiTietService;

    @Autowired
    SanPhamRepository sanPhamRepository;

    @Autowired
    KhachHangService khachHangService;

    @Autowired
    GioHangService gioHangService;

    @GetMapping("/san-pham/hien-thi")
    public String getSanPhamChiTiet(@AuthenticationPrincipal UserDetails userDetails, Model model) {
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

        List<SanPhamChiTiet> sanPhamChiTietList = sanPhamChiTietService.getAllSanPhamChiTiet();
        model.addAttribute("sanPhamChiTietList", sanPhamChiTietList);
        return "customer/san-pham";
    }

    @GetMapping("/san-pham/loai/{idLoaiSP}")
    public String getSanPhamByLoai(@PathVariable int idLoaiSP, Model model) {
        List<SanPhamChiTiet> sanPhamChiTietList = sanPhamChiTietService.findByLoaiSanPham(idLoaiSP);
        model.addAttribute("sanPhamChiTietList", sanPhamChiTietList);
        return "customer/san-pham"; // Trả về tên template HTML của bạn
    }

//    @GetMapping("san-pham/hien-thi")
//    public String hienThi(Model model) {
//        model.addAttribute("sanPhams", sanPhamRepository.findAll());
//        return "customer/san-pham";
//    }

    @GetMapping("nhan-vien/hien-thi")
    public String getSanPhamChiTiet1(Model model) {
        List<SanPhamChiTiet> sanPhamChiTietList = sanPhamChiTietService.getAllSanPhamChiTiet();
        model.addAttribute("sanPhamChiTietList", sanPhamChiTietList);
        return "nhanvien/productProvity";
    }

    @GetMapping("/nhan-vien/loai/{idLoaiSP}")
    public String getSanPhamByLoai1(@PathVariable int idLoaiSP, Model model) {
        List<SanPhamChiTiet> sanPhamChiTietList = sanPhamChiTietService.findByLoaiSanPham(idLoaiSP);
        model.addAttribute("sanPhamChiTietList", sanPhamChiTietList);
        return "nhanvien/productProvity"; // Trả về tên template HTML của bạn
    }
}

