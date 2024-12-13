package com.example.demo.service;

import com.example.demo.entity.GioHang;
import com.example.demo.entity.HoaDon;
import com.example.demo.entity.HoaDonChiTiet;
import com.example.demo.repository.HoaDonChiTietRepository;
import com.example.demo.repository.HoaDonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public void updateTongGia(HoaDon hoaDon) {
        // Kiểm tra nếu giỏ hàng không trống
        if (hoaDon.getHoaDonChiTietList() == null) {
            hoaDon.setTongGia(0.0); // Nếu giỏ hàng trống, set tổng giá là 0
        } else {
            // Tính tổng giá từ các sản phẩm còn lại trong giỏ hàng
            Double tongGia = hoaDonChiTietRepository.calculateTotalPrice(hoaDon.getId());

            // Nếu tổng giá tính ra là null, thì gán về 0.0
            if (tongGia == null) {
                tongGia = 0.0;
            }

            hoaDon.setTongGia(tongGia); // Cập nhật tổng giá
        }

        hoaDonRepository.save(hoaDon);
    }
}
