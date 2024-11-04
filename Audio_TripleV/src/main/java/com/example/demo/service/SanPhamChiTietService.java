package com.example.demo.service;

import com.example.demo.entity.SanPhamChiTiet;
import com.example.demo.repository.SanPhamChiTietRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SanPhamChiTietService {

    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;

    // Update stock after order confirmation
    public void updateStock(int sanPhamChiTietId, int quantity) {
        // Find the product
        SanPhamChiTiet product = sanPhamChiTietRepository.findById(sanPhamChiTietId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Update the stock
        product.setSoLuong(product.getSoLuong() - quantity);

        // Save the updated product
        sanPhamChiTietRepository.save(product);
    }

    public SanPhamChiTiet findById(Integer id) {
      return sanPhamChiTietRepository.findById(id).orElse(null);


    }
}
