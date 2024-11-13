package com.example.demo.repository;


import com.example.demo.entity.Hang;
import com.example.demo.entity.LoaiSanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoaiSanPhamRepository extends JpaRepository<LoaiSanPham, Integer> {
    @Query("SELECT DISTINCT s.loaiSanPham FROM SanPhamChiTiet s WHERE " +
            "(:sanPhamTen IS NULL OR s.sanPham.ten LIKE %:sanPhamTen%) AND " +
            "(:mauSac IS NULL OR s.mauSac.id = :mauSac) AND " +
            "(:hang IS NULL OR s.hang.id = :hang) AND " +
            "(:minPrice IS NULL OR s.donGia >= :minPrice) AND " +
            "(:maxPrice IS NULL OR s.donGia <= :maxPrice)")
    List<LoaiSanPham> findAvailableLoaiSanPhams(String sanPhamTen, Integer mauSac, Integer hang, Double minPrice, Double maxPrice);
}
