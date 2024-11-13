package com.example.demo.repository;

import com.example.demo.entity.Hang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HangRepository extends JpaRepository<Hang,Integer> {
    @Query("SELECT DISTINCT s.hang FROM SanPhamChiTiet s WHERE " +
            "(:sanPhamTen IS NULL OR s.sanPham.ten LIKE %:sanPhamTen%) AND " +
            "(:idLoaiSP IS NULL OR s.loaiSanPham.id = :idLoaiSP) AND " +
            "(:mauSac IS NULL OR s.mauSac.id = :mauSac) AND " +
            "(:minPrice IS NULL OR s.donGia >= :minPrice) AND " +
            "(:maxPrice IS NULL OR s.donGia <= :maxPrice)")
    List<Hang> findAvailableHangs(String sanPhamTen, Integer idLoaiSP, Integer mauSac, Double minPrice, Double maxPrice);

}
