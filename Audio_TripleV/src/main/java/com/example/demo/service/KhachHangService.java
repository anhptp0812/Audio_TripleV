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

    public List<KhachHang> searchByNameAndPhone(String name, String phone) {
        if (name != null && phone != null) {
            return khachHangRepo.findByTenContainingAndSdtContaining(name, phone);
        } else if (name != null) {
            return khachHangRepo.findByTenContaining(name);
        } else {
            return khachHangRepo.findBySdtContaining(phone);
        }
    }
}
