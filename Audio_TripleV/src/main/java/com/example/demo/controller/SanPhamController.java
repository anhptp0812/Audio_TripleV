package com.example.demo.controller;

import com.example.demo.repository.SanPhamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SanPhamController {

    @Autowired
    SanPhamRepository sanPhamRepository;

    @GetMapping("san-pham/hien-thi")
    public String hienThi(Model model) {
        model.addAttribute("sanPhams", sanPhamRepository.findAll());
        return "customer/san-pham";
    }
    @GetMapping("san-pham/tai-nghe-day")
    public String hienThi1(Model model) {

        return "customer/tai-nghe-day";
    }
    @GetMapping("san-pham/tai-nghe-gaming")
    public String hienThi2(Model model) {

        return "customer/tai-nghe-gaming";
    }
    @GetMapping("san-pham/tai-nghe-bluetooth")
    public String hienThi3(Model model) {

        return "customer/tai-nghe-bluetooth";
    }


}

