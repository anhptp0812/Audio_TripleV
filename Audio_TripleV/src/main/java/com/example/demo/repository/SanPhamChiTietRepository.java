package com.example.demo.repository;

import com.example.demo.entity.SanPhamChiTiet;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SanPhamChiTietRepository extends JpaRepository<SanPhamChiTiet,Integer> {
    List<SanPhamChiTiet> findByLoaiSanPham_Id(int idLoaiSP);
    @Query("SELECT s FROM SanPhamChiTiet s ORDER BY s.ngayTao DESC")
    List<SanPhamChiTiet> findTop4NewestProducts(Pageable pageable);
    List<SanPhamChiTiet> findTop4ByOrderByIdDesc();
}
