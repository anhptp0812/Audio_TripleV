package com.example.demo.service;

import com.example.demo.entity.SanPhamChiTiet;
import com.example.demo.repository.SanPhamChiTietRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
//Thanh


import java.util.Date;
import java.util.List;
import java.util.Optional;
 //main

@Service
public class    SanPhamChiTietService {
    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;


    public List<SanPhamChiTiet> getAllSanPhamChiTiet() {
        return sanPhamChiTietRepository.findAll();
    }
    public Page<SanPhamChiTiet> findByLoaiSanPham(int idLoaiSP, int page, int size) {
        Pageable pageable = PageRequest.of(page, size); // Tạo Pageable với trang và kích thước
        return sanPhamChiTietRepository.findByLoaiSanPham_Id(idLoaiSP, pageable);
    }


    // Lấy danh sách sản phẩm chi tiết với phân trang
    public Page<SanPhamChiTiet> getSanPhamChiTietWithPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size); // page bắt đầu từ 0
        return sanPhamChiTietRepository.findAll(pageable);
    }

    public List<SanPhamChiTiet> getTop4NewestProducts() {
        return sanPhamChiTietRepository.findTop4NewestProducts(PageRequest.of(0, 4));
    }

    public List<SanPhamChiTiet> getTop4SanPhamChiTiet() {
        return sanPhamChiTietRepository.findTop4ByOrderByIdDesc();
    }

    // Tìm sản phẩm theo ID
    public SanPhamChiTiet findById(Integer id) {
        return sanPhamChiTietRepository.findById(id).orElse(null);

    }

    // Xóa sản phẩm theo ID
    public void deleteById(Integer id) {
        sanPhamChiTietRepository.deleteById(id);
    }

    public SanPhamChiTiet updateSanPhamChiTiet(SanPhamChiTiet sanPhamChiTiet) {
        // Kiểm tra nếu không có ngày cập nhật được cung cấp từ phía frontend, thì gán ngày hiện tại
        if (sanPhamChiTiet.getNgayCapNhat() == null) {
            sanPhamChiTiet.setNgayCapNhat(new Date()); // Gán ngày hiện tại
        }

        // Thực hiện cập nhật sản phẩm chi tiết vào cơ sở dữ liệu
        return sanPhamChiTietRepository.save(sanPhamChiTiet);
    }

    // Phương thức thêm sản phẩm chi tiết
    public SanPhamChiTiet addSanPhamChiTiet(SanPhamChiTiet sanPhamChiTiet) {
        // Thêm sản phẩm chi tiết vào cơ sở dữ liệu
        return sanPhamChiTietRepository.save(sanPhamChiTiet);
    }

    public List<SanPhamChiTiet> findByHangId(Integer hangId) {
        // Giả sử bạn có một repository để truy vấn sản phẩm theo hãng
        return sanPhamChiTietRepository.findByHangId(hangId);
    }
}
