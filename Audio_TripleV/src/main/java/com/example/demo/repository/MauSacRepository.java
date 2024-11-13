package com.example.demo.repository;

import com.example.demo.entity.MauSac;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MauSacRepository extends JpaRepository<MauSac, Integer> {
    @Query("SELECT DISTINCT s.mauSac FROM SanPhamChiTiet s WHERE " +
            "(:sanPhamTen IS NULL OR s.sanPham.ten LIKE %:sanPhamTen%) AND " +
            "(:idLoaiSP IS NULL OR s.loaiSanPham.id = :idLoaiSP) AND " +
            "(:hang IS NULL OR s.hang.id = :hang) AND " +
            "(:minPrice IS NULL OR s.donGia >= :minPrice) AND " +
            "(:maxPrice IS NULL OR s.donGia <= :maxPrice)")
    List<MauSac> findAvailableMauSacs(String sanPhamTen, Integer idLoaiSP, Integer hang, Double minPrice, Double maxPrice);
}
