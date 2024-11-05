package com.example.demo.service;

import com.example.demo.entity.LoaiSanPham;
import com.example.demo.repository.LoaiSanPhamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoaiSanPhamService {
    
    @Autowired
    private LoaiSanPhamRepository loaiSanPhamRepository;


    public List<LoaiSanPham> findAll() {
        return loaiSanPhamRepository.findAll();
    }


    public void save(LoaiSanPham loaiSanPham) {
        loaiSanPhamRepository.save(loaiSanPham);
    }


    public LoaiSanPham findById(Integer id) {
        return loaiSanPhamRepository.findById(id).orElse(null);
    }

    public LoaiSanPham updateLoaiSanPham(LoaiSanPham loaiSanPham) {
        // Kiểm tra tồn tại trước khi cập nhật
        if (loaiSanPhamRepository.existsById(loaiSanPham.getId())) {
            return loaiSanPhamRepository.save(loaiSanPham);
        } else {
            throw new RuntimeException("Không tìm thấy hãng với ID: " + loaiSanPham.getId());
        }
    }

    public void deleteById(Integer id) {
        loaiSanPhamRepository.deleteById(id);
    }
}
