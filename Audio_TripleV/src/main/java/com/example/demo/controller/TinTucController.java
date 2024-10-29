package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TinTucController {

    @GetMapping("trang-chu/hien-thi")
    public String hienThiThuongHieu(Model model) {

        return "customer/tin-tuc";
    }
}
