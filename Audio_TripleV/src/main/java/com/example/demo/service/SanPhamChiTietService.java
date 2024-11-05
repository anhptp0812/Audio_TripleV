package com.example.demo.service;

import com.example.demo.entity.SanPhamChiTiet;
import com.example.demo.repository.SanPhamChiTietRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
Thanh


import java.util.Date;
import java.util.List;
import java.util.Optional;
 main

@Service
public class SanPhamChiTietService {
    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;

    public List<SanPhamChiTiet> getAllSanPhamChiTiet() {
        return sanPhamChiTietRepository.findAll();
    }
    public List<SanPhamChiTiet> findByLoaiSanPham(int idLoaiSP) {
        return sanPhamChiTietRepository.findByLoaiSanPham_Id(idLoaiSP);
    }

    public List<SanPhamChiTiet> getTop4NewestProducts() {
        return sanPhamChiTietRepository.findTop4NewestProducts(PageRequest.of(0, 4));
    }

    public List<SanPhamChiTiet> getTop4SanPhamChiTiet() {
        return sanPhamChiTietRepository.findTop4ByOrderByIdDesc();
    }

    // Tìm sản phẩm theo ID
    public SanPhamChiTiet findById(Integer id) {
 Thanh
        return sanPhamChiTietRepository.findById(id).orElse(null);

        Optional<SanPhamChiTiet> optionalSpct = sanPhamChiTietRepository.findById(id);
        return optionalSpct.orElse(null); // Trả về sản phẩm nếu tìm thấy, ngược lại trả về null
    }

    // Xóa sản phẩm theo ID
    public void deleteById(Integer id) {
        sanPhamChiTietRepository.deleteById(id);
    }

    // Phương thức cập nhật sản phẩm chi tiết
//    public SanPhamChiTiet updateSanPhamChiTiet(SanPhamChiTiet sanPhamChiTiet) {
//        // Kiểm tra xem sản phẩm chi tiết có tồn tại không
//        if (sanPhamChiTietRepository.existsById(sanPhamChiTiet.getId())) {
//            return sanPhamChiTietRepository.save(sanPhamChiTiet);
//        } else {
//            throw new RuntimeException("Sản phẩm chi tiết không tồn tại!");
//        }
//    }
    public SanPhamChiTiet updateSanPhamChiTiet(SanPhamChiTiet sanPhamChiTiet) {
        // Kiểm tra nếu không có ngày cập nhật được cung cấp từ phía frontend, thì gán ngày hiện tại
        if (sanPhamChiTiet.getNgayCapNhat() == null) {
            sanPhamChiTiet.setNgayCapNhat(new Date()); // Gán ngày hiện tại
        }

        // Thực hiện cập nhật sản phẩm chi tiết vào cơ sở dữ liệu
        return sanPhamChiTietRepository.save(sanPhamChiTiet);
    }
 main


    // Phương thức thêm sản phẩm chi tiết
    public SanPhamChiTiet addSanPhamChiTiet(SanPhamChiTiet sanPhamChiTiet) {
        // Thêm sản phẩm chi tiết vào cơ sở dữ liệu
        return sanPhamChiTietRepository.save(sanPhamChiTiet);
    }
}
