package com.example.demo.repository;

import com.example.demo.entity.GioHang;
import com.example.demo.entity.GioHangChiTiet;
import com.example.demo.entity.SanPhamChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GioHangChiTietRepository extends JpaRepository<GioHangChiTiet, Integer> {
    @Query("SELECT SUM(gct.soLuong * gct.sanPhamChiTiet.donGia) FROM GioHangChiTiet gct WHERE gct.gioHang.id = :gioHangId")
    Double calculateTotalPrice(@Param("gioHangId") Integer gioHangId);

    Optional<GioHangChiTiet> findByGioHangAndSanPhamChiTiet(GioHang gioHang, SanPhamChiTiet sanPhamChiTiet);
    Optional<GioHangChiTiet> findBySanPhamChiTiet_Id(Long productId);


}

