package com.example.demo.controller;

import com.example.demo.entity.SanPhamChiTiet;
import com.example.demo.service.GioHangService;
import com.example.demo.service.SanPhamChiTietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/gio-hang")
public class GioHangController {

    private final GioHangService gioHangService;

    private SanPhamChiTiet sanPhamChiTiet;

    @Autowired
    public GioHangController(GioHangService gioHangService) {
        this.gioHangService = gioHangService;
    }

    @Autowired
    SanPhamChiTietService sanPhamChiTietService;

    @GetMapping("/hien-thi")
    public String hienThiGioHang(Model model) {
        List<SanPhamChiTiet> sanPhamChiTietList = sanPhamChiTietService.getAllSanPhamChiTiet();
        model.addAttribute("cartItems", sanPhamChiTietList);
        return "customer/gio-hang"; // Chuyển đến trang hiển thị giỏ hàng
    }

}
