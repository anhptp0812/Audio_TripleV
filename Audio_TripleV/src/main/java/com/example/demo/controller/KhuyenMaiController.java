package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("khach-hang")
public class KhuyenMaiController {
    @GetMapping("/khuyen-mai/hien-thi")
    public String hienThiKhuyenMai(Model model) {

        return "customer/khuyen-mai";
    }
}
