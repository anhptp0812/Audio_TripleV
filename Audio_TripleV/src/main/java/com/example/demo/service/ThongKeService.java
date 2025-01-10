package com.example.demo.service;

import com.example.demo.repository.HoaDonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ThongKeService {

    @Autowired
    private HoaDonRepository hoaDonRepository;

    public Double tinhTongDoanhThuTheoNgay(LocalDate date) {
        return hoaDonRepository.getTongDoanhThuTheoNgay(date);
    }

    public Double tinhTongDoanhThuTheoThang(int month, int year) {
        return hoaDonRepository.getTongDoanhThuTheoThang(month, year);
    }

    public Double tinhTongDoanhThuTheoNam(int year) {
        return hoaDonRepository.getTongDoanhThuTheoNam(year);
    }
}
