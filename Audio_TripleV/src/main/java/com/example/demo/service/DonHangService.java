package com.example.demo.service;

import com.example.demo.entity.DonHang;
import com.example.demo.entity.DonHangChiTiet;

import com.example.demo.entity.GioHang;
import com.example.demo.entity.KhachHang;
import com.example.demo.entity.SanPhamChiTiet;

import com.example.demo.repository.DonHangRepository;
import com.example.demo.repository.DonHangChiTietRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

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

    public DonHang createOrder(KhachHang khachHang, SanPhamChiTiet sanPhamChiTiet, Integer quantity) {
        // Tính tổng giá
        double totalPrice = sanPhamChiTiet.getDonGia() * quantity;

        // Tạo đơn hàng mới
        DonHang donHang = new DonHang();
        donHang.setKhachHang(khachHang);
        donHang.setTongGia(totalPrice);
        donHang.setTrangThai("Chờ xử lý");
        donHang.setNgayTao(new Date());
        donHang.setNgayCapNhat(new Date());
        donHang = donHangRepository.save(donHang);

        // Tạo chi tiết đơn hàng
        DonHangChiTiet chiTiet = new DonHangChiTiet();
        chiTiet.setDonHang(donHang);
        chiTiet.setSanPhamChiTiet(sanPhamChiTiet);
        chiTiet.setSoLuong(quantity);
        chiTiet.setDonGia(sanPhamChiTiet.getDonGia());
        chiTiet.setNgayTao(new Date());
        chiTiet.setNgayCapNhat(new Date());
        donHangChiTietRepository.save(chiTiet);

        return donHang;
    }

    public DonHang save(DonHang donHang) {
        return donHangRepository.save(donHang);
    }

    // Tìm giỏ hàng của khách hàng
    public List<DonHang> findByKhachHang(KhachHang khachHang) {
        List<DonHang> donHangList = donHangRepository.findByKhachHang(khachHang);
        for (DonHang donHang : donHangList) {
            // Đảm bảo danh sách chi tiết đơn hàng được tải về
            donHang.getDonHangChiTietList().size();
        }
        return donHangList;
    }
}
