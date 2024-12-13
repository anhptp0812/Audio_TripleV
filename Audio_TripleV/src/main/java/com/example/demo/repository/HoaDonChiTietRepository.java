package com.example.demo.repository;

import com.example.demo.entity.HoaDon;
import com.example.demo.entity.HoaDonChiTiet;
import com.example.demo.entity.SanPhamChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HoaDonChiTietRepository extends JpaRepository<HoaDonChiTiet, Integer> {
    List<HoaDonChiTiet> findByHoaDon_Id(Integer id);

    Optional<HoaDonChiTiet> deleteByHoaDon_Id(Integer integer);

    Optional<HoaDonChiTiet> findByHoaDonAndSanPhamChiTiet(HoaDon hoaDon, SanPhamChiTiet sanPhamChiTiet);

    List<HoaDonChiTiet> findByHoaDon(HoaDon hoaDon);

    @Query("SELECT SUM(hdct.soLuong * hdct.donGia) FROM HoaDonChiTiet hdct WHERE hdct.hoaDon.id = :hoaDonId")
    Double calculateTotalPrice(@Param("hoaDonId") Integer hoaDonId);

    // Optional<HoaDonChiTiet> findBySanPhamChiTiet(Integer integer);
}
