package com.example.demo.service;

import com.example.demo.entity.HoaDon;
import com.example.demo.entity.HoaDonChiTiet;
import com.example.demo.repository.HoaDonChiTietRepository;
import com.example.demo.repository.HoaDonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HoaDonService {

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Autowired
    private HoaDonChiTietRepository hoaDonChiTietRepository;


    public HoaDon findByid(Integer id) {
        return hoaDonRepository.findById(id).orElse(null);
    }

    public List<HoaDonChiTiet> findByDHid(Integer id) {
        return hoaDonChiTietRepository.findByHoaDon_Id(id);
    }
}
