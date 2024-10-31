package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ThuongHieuController {

//    @Autowired
//    HangRepository hangRepository;

    @GetMapping("thuong-hieu/hien-thi")
    public String hienThiThuongHieu(Model model) {
//        model.addAttribute("hangs", hangRepository.findAll());
        return "customer/thuong-hieu";
    }
    @GetMapping("thuong-hieu/note-thuong-hieu")
    public String hienThiNoteThuongHieu(Model model) {

        return "customer/note-thuong-hieu";
    }
}
