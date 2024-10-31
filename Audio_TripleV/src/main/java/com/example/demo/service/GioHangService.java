package com.example.demo.service;

import com.example.demo.entity.GioHang;
import com.example.demo.repository.GioHangRepository;
import com.example.demo.repository.SanPhamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GioHangService {
    @Autowired
    SanPhamRepository sanPhamRepository;

    private final GioHangRepository gioHangRepository;

    public GioHangService(GioHangRepository gioHangRepository) {
        this.gioHangRepository = gioHangRepository;
    }

    public List<GioHang> getAllGioHang() {
        return gioHangRepository.findAll();
    }

    public void themGioHang(GioHang gioHang) {
        gioHangRepository.save(gioHang);
    }

    public GioHang getGioHangById(Integer id) {
        return gioHangRepository.findById(id).orElse(null);
    }

    public void suaGioHang(GioHang gioHang) {
        gioHangRepository.save(gioHang);
    }

    public void xoaGioHang(Integer id) {
        gioHangRepository.deleteById(id);
    }

}
