package com.example.demo.repository;

import com.example.demo.entity.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HoaDonRepository extends JpaRepository<HoaDon, Integer> {
    List<HoaDon> findByTrangThai(String trangThai);

    @Query("SELECT SUM(hd.tongGia) FROM HoaDon hd WHERE cast(hd.ngayTao AS DATE) = :date")
    Double getTongDoanhThuTheoNgay(@Param("date") LocalDate date);

    @Query("SELECT SUM(hd.tongGia) FROM HoaDon hd WHERE MONTH(hd.ngayTao) = :month AND YEAR(hd.ngayTao) = :year")
    Double getTongDoanhThuTheoThang(@Param("month") int month, @Param("year") int year);

    @Query("SELECT SUM(hd.tongGia) FROM HoaDon hd WHERE YEAR(hd.ngayTao) = :year")
    Double getTongDoanhThuTheoNam(@Param("year") int year);
}
