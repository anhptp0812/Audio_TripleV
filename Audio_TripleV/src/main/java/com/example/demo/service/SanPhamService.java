package com.example.demo.service;
import com.example.demo.entity.SanPham;
import com.example.demo.repository.SanPhamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class SanPhamService {
    @Autowired
    private SanPhamRepository sanPhamRepository;

    public Page<SanPham> getSanPham(int page, int size) {
        return sanPhamRepository.findAll(PageRequest.of(page, size));
    }
}
