package com.example.demo.service;

import com.example.demo.entity.Hang;
import com.example.demo.repository.HangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HangService {
    
    @Autowired
    private HangRepository hangRepository;

    // Lấy danh sách tất cả màu sắc
    public List<Hang> findAll() {
        return hangRepository.findAll();
    }

    // Lưu màu sắc mới
    public void save(Hang hang) {
        hangRepository.save(hang);
    }

    public Hang findById(Integer id) {
        return hangRepository.findById(id).orElse(null);
    }

    public Hang updateHang(Hang hang) {
        // Kiểm tra tồn tại trước khi cập nhật
        if (hangRepository.existsById(hang.getId())) {
            return hangRepository.save(hang);
        } else {
            throw new RuntimeException("Không tìm thấy hãng với ID: " + hang.getId());
        }
    }

    // Xóa màu sắc theo ID
    public void deleteById(Integer id) {
        hangRepository.deleteById(id);
    }
}
