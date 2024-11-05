package com.example.demo.service;

import com.example.demo.entity.DonHang;
import com.example.demo.entityCustom.DonHangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DonHangService {

    @Autowired
    private DonHangRepository donHangRepository;

    public DonHang findByid(Integer id) {
        return donHangRepository.findById(id).orElse(null);
    }


}
