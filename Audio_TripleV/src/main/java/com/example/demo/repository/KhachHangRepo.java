package com.example.demo.repository;

import com.example.demo.entity.KhachHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KhachHangRepo extends JpaRepository<KhachHang, Integer> {
 List<KhachHang> findByTenContaining(String name);
 List<KhachHang> findBySdtContaining(String phone);
 List<KhachHang> findByTenContainingAndSdtContaining(String name, String phone);

}
