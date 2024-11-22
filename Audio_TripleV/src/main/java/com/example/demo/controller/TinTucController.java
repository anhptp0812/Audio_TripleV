package com.example.demo.controller;

import com.example.demo.entity.KhachHang;
import com.example.demo.entity.SanPhamChiTiet;
import com.example.demo.repository.KhachHangRepository;
import com.example.demo.service.SanPhamChiTietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
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
    KhachHangRepository khachHangRepository;

    @GetMapping("/trang-chu/hien-thi")
    public String showTop4SanPhamMoiAndSanPhamXemNhieuNhat(@AuthenticationPrincipal User user, Model model) {
        String username = user.getUsername();
        Optional<KhachHang> optionalKhachHang = khachHangRepository.findByTaiKhoan(username);
        if (optionalKhachHang.isPresent()) {
            KhachHang khachHang = optionalKhachHang.get();
            model.addAttribute("fullName", khachHang.getTen());
        }

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
