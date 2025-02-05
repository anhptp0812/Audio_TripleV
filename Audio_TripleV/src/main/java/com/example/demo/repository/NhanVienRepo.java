package com.example.demo.repository;

import com.example.demo.entity.KhachHang;
import com.example.demo.entity.NhanVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NhanVienRepo extends JpaRepository<NhanVien, Integer> {
    Optional<NhanVien> findByUsername(String username);
    List<NhanVien> findByTenContaining(String name);

    @Query("SELECT DISTINCT nv.role FROM NhanVien nv")
    List<String> findAllRoles();  // Truy vấn tất cả các vai trò
}
