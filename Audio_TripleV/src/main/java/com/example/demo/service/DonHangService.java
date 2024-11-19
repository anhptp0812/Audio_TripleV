package com.example.demo.service;

import com.example.demo.entity.DonHang;
import com.example.demo.entity.DonHangChiTiet;
import com.example.demo.repository.DonHangRepository;
import com.example.demo.repository.DonHangChiTietRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DonHangService {

    @Autowired
    private DonHangRepository donHangRepository;

    @Autowired
    private DonHangChiTietRepository donHangChiTietRepository;

    public DonHang findByid(Integer id) {
        return donHangRepository.findById(id).orElse(null);
    }

    public List<DonHangChiTiet> findByDHid(Integer id) {
        return donHangChiTietRepository.findByDonHang_Id(id);
    }

}
