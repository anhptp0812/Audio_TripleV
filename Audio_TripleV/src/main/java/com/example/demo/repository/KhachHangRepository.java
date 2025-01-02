package com.example.demo.repository;

import com.example.demo.entity.KhachHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KhachHangRepository extends JpaRepository<KhachHang, Integer> {
    // Tìm khách hàng theo tài khoản
    Optional<KhachHang> findByTaiKhoan(String taiKhoan);

    // Tìm khách hàng theo tên
    List<KhachHang> findByTenContaining(String name);

    // Tìm khách hàng theo số điện thoại
    List<KhachHang> findBySdtContaining(String phone);

    // Tìm khách hàng theo tên và số điện thoại
    List<KhachHang> findByTenContainingAndSdtContaining(String ten, String sdt);

    // Tìm khách hàng theo số điện thoại (chính xác)
    Optional<KhachHang> findBySdt(String sdt);

    Optional<Object> findByTen(String ten);
    @Query("SELECT DISTINCT k.role FROM KhachHang k")
    List<String> findAllRoles();  // Truy vấn tất cả các vai trò


    // Tìm khách hàng theo tên (chính xác)
    Optional<KhachHang> findByTen(String ten);

}

