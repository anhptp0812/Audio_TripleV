package com.example.demo.controller;

import com.example.demo.entity.GioHangChiTiet;
import com.example.demo.service.GioHangChiTietService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/gio-hang-chi-tiet")
public class GioHangChiTietController {

    private final GioHangChiTietService gioHangChiTietService;

    public GioHangChiTietController(GioHangChiTietService gioHangChiTietService) {
        this.gioHangChiTietService = gioHangChiTietService;
    }

    @GetMapping("/hien-thi")
    public String hienThiGioHangChiTiet(Model model) {
        List<GioHangChiTiet> gioHangChiTietList = gioHangChiTietService.getAllGioHangChiTiet();
        model.addAttribute("gioHangChiTietList", gioHangChiTietList);
        return "customer/gio-hang-chi-tiet"; // Chuyển đến trang hiển thị chi tiết giỏ hàng
    }

    @GetMapping("/them")
    public String themGioHangChiTietForm(Model model) {
        model.addAttribute("gioHangChiTiet", new GioHangChiTiet());
        return "customer/gio-hang-chi-tiet-them"; // Chuyển đến trang thêm chi tiết giỏ hàng
    }

    @PostMapping("/them")
    public String themGioHangChiTiet(@ModelAttribute GioHangChiTiet gioHangChiTiet) {
        gioHangChiTietService.themGioHangChiTiet(gioHangChiTiet);
        return "redirect:/gio-hang-chi-tiet/hien-thi"; // Quay lại trang hiển thị chi tiết giỏ hàng
    }

    @GetMapping("/sua/{id}")
    public String suaGioHangChiTietForm(@PathVariable Integer id, Model model) {
        GioHangChiTiet gioHangChiTiet = gioHangChiTietService.getGioHangChiTietById(id);
        model.addAttribute("gioHangChiTiet", gioHangChiTiet);
        return "customer/gio-hang-chi-tiet-sua"; // Chuyển đến trang sửa chi tiết giỏ hàng
    }

    @PostMapping("/sua")
    public String suaGioHangChiTiet(@ModelAttribute GioHangChiTiet gioHangChiTiet) {
        gioHangChiTietService.suaGioHangChiTiet(gioHangChiTiet);
        return "redirect:/gio-hang-chi-tiet/hien-thi"; // Quay lại trang hiển thị chi tiết giỏ hàng
    }

    @GetMapping("/xoa/{id}")
    public String xoaGioHangChiTiet(@PathVariable Integer id) {
        gioHangChiTietService.xoaGioHangChiTiet(id);
        return "redirect:/gio-hang-chi-tiet/hien-thi"; // Quay lại trang hiển thị chi tiết giỏ hàng
    }
}
