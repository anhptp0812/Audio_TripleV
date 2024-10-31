package com.example.demo.controller;

import com.example.demo.entity.SanPhamChiTiet;
import com.example.demo.service.SanPhamChiTietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class TinTucController {
    @Autowired
    SanPhamChiTietService sanPhamChiTietService;

    @GetMapping("/trang-chu/hien-thi")
    public String showTop4SanPhamMoiAndSanPhamXemNhieuNhat(Model model) {
        List<SanPhamChiTiet> newestProducts = sanPhamChiTietService.getTop4NewestProducts();
        model.addAttribute("sanPhamChiTietList", newestProducts);
        List<SanPhamChiTiet> sanPhamChiTietList = sanPhamChiTietService.getTop4SanPhamChiTiet();
        model.addAttribute("sanPhamChiTietList1", sanPhamChiTietList);
        return "customer/tin-tuc";
    }
}
