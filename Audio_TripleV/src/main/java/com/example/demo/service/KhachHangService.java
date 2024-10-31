package com.example.demo.service;

import com.example.demo.entity.KhachHang;
import com.example.demo.repository.KhachHangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class KhachHangService {

    @Autowired
    private KhachHangRepository khachHangRepository;

    // Lưu hoặc cập nhật thông tin khách hàng
    public KhachHang save(KhachHang khachHang) {
        return khachHangRepository.save(khachHang);
    }

    // Tìm khách hàng theo ID
    public Optional<KhachHang> findById(Integer id) {
        return khachHangRepository.findById(id);
    }

    // Tìm khách hàng theo tài khoản
    public Optional<KhachHang> findByTaiKhoan(String taiKhoan) {
        return khachHangRepository.findByTaiKhoan(taiKhoan);
    }

    // Xóa khách hàng theo ID
    public void deleteById(Integer id) {
        khachHangRepository.deleteById(id);
    }
}

