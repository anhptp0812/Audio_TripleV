package com.example.demo.service;

import com.example.demo.entity.MauSac;
import com.example.demo.repository.MauSacRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MauSacService {
    
    @Autowired
    private MauSacRepository mauSacRepository;

    // Lấy danh sách tất cả màu sắc
    public List<MauSac> findAll() {
        return mauSacRepository.findAll();
    }

    // Lưu màu sắc mới
    public void save(MauSac mauSac) {
        mauSacRepository.save(mauSac);
    }

    // Xóa màu sắc theo ID
    public void deleteById(Integer id) {
        mauSacRepository.deleteById(id);
    }


    public MauSac findById(Integer id) {
        return mauSacRepository.findById(id).orElse(null);
    }

    public MauSac updateMauSac(MauSac mauSac) {
        // Kiểm tra tồn tại trước khi cập nhật
        if (mauSacRepository.existsById(mauSac.getId())) {
            return mauSacRepository.save(mauSac);
        } else {
            throw new RuntimeException("Không tìm thấy màu sắc với ID: " + mauSac.getId());
        }
    }
}
