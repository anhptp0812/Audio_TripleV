package com.example.demo.controller;

import com.example.demo.entity.KhachHang;
import com.example.demo.repository.KhachHangRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class KhachHangController {
    @Autowired
    KhachHangRepo khachHangRepo;

    @GetMapping("/admin/khach-hang")
    public String getAllKhachHang(Model model) {
        List<KhachHang> list = khachHangRepo.findAll();
        model.addAttribute("data", list);
        return "admin/khach_hang"; // Trả về template HTML
    }


}
