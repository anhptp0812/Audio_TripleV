package com.example.demo.controller;

import com.example.demo.entity.KhachHang;
import com.example.demo.repository.KhachHangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class KhachHangController {
    @Autowired
    KhachHangRepository khachHangRepository;

    @GetMapping("hien-thi")
    public List<KhachHang> hienThiKhachHang() {

        return khachHangRepository.findAll();
    }


}
