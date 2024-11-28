package com.example.demo.service;

import com.example.demo.entity.HinhAnh;
import com.example.demo.repository.HinhAnhRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HinhAnhService {
    
    @Autowired
    private HinhAnhRepository hinhAnhRepository;

    // Lấy danh sách tất cả màu sắc
    public List<HinhAnh> findAll() {
        return hinhAnhRepository.findAll();
    }

    // Lưu màu sắc mới
    @Transactional
    public void save(HinhAnh hinhAnh) {
        hinhAnhRepository.save(hinhAnh);
    }

    // Xóa màu sắc theo ID
    public void deleteById(Integer id) {
        hinhAnhRepository.deleteById(id);
    }


    public HinhAnh findById(Integer id) {
        return hinhAnhRepository.findById(id).orElse(null);
    }

    public HinhAnh updateHinhAnh(HinhAnh hinhAnh) {
        // Kiểm tra tồn tại trước khi cập nhật
        if (hinhAnhRepository.existsById(hinhAnh.getId())) {
            return hinhAnhRepository.save(hinhAnh);
        } else {
            throw new RuntimeException("Không tìm thấy hình ảnh với ID: " + hinhAnh.getId());
        }
    }
}
