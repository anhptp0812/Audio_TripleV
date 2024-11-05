package com.example.demo.repository;

import com.example.demo.entity.KhachHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KhachHangRepository extends JpaRepository<KhachHang, Integer> {
    // Tìm khách hàng theo tài khoản
    Optional<KhachHang> findByTaiKhoan(String taiKhoan);
    List<KhachHang> findByTenContaining(String name);
    List<KhachHang> findBySdtContaining(String phone);
    List<KhachHang> findByTenContainingAndSdtContaining(String name, String phone);
}

