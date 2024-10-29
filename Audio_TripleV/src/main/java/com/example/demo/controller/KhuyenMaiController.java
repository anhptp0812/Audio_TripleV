package com.example.demo.controller;

import com.example.demo.entity.KhuyenMai;
import com.example.demo.repository.KhuyenMaiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class KhuyenMaiController {
    @Autowired
    KhuyenMaiRepository khuyenMaiRepository;
    @GetMapping("/admin/khuyen-mai")
    public String getAllKhachHang(Model model) {
        List<KhuyenMai> list = khuyenMaiRepository.findAll();
        model.addAttribute("data", list);
        return "admin/khuyen_mai"; // Trả về template HTML
    }
}
