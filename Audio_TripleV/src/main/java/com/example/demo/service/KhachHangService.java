package com.example.demo.service;

import com.example.demo.entity.KhachHang;
import com.example.demo.repository.KhachHangRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class KhachHangService {
    @Autowired
    private KhachHangRepo khachHangRepo;

    public List<KhachHang> getAllKhachHang() {
        return khachHangRepo.findAll();
    }
}
