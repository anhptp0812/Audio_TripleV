package com.example.demo.service;

import com.example.demo.entity.GioHangChiTiet;
import com.example.demo.repository.GioHangChiTietRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GioHangChiTietService {

    private final GioHangChiTietRepository gioHangChiTietRepository;

    public GioHangChiTietService(GioHangChiTietRepository gioHangChiTietRepository) {
        this.gioHangChiTietRepository = gioHangChiTietRepository;
    }

    public List<GioHangChiTiet> getAllGioHangChiTiet() {
        return gioHangChiTietRepository.findAll();
    }

    public void themGioHangChiTiet(GioHangChiTiet gioHangChiTiet) {
        gioHangChiTietRepository.save(gioHangChiTiet);
    }

    public GioHangChiTiet getGioHangChiTietById(Integer id) {
        return gioHangChiTietRepository.findById(id).orElse(null);
    }

    public void suaGioHangChiTiet(GioHangChiTiet gioHangChiTiet) {
        gioHangChiTietRepository.save(gioHangChiTiet);
    }

    public void xoaGioHangChiTiet(Integer id) {
        gioHangChiTietRepository.deleteById(id);
    }


}
