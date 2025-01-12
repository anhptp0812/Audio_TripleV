package com.example.demo.repository;

import com.example.demo.entity.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface HoaDonRepository extends JpaRepository<HoaDon, Integer> {
    // Tìm hóa đơn theo trạng thái
    List<HoaDon> findByTrangThai(String trangThai);

    // Tổng doanh thu theo ngày (tìm trong khoảng ngày)
    @Query("SELECT COUNT(hd) FROM HoaDon hd WHERE hd.ngayTao BETWEEN :startDate AND :endDate")
    int countByNgayTaoBetween(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT COALESCE(SUM(hd.soTienPhaiTra), 0.0) FROM HoaDon hd WHERE hd.ngayTao BETWEEN :startDate AND :endDate")
    Double getTongDoanhThuTheoNgay(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT SUM(hd.soTienPhaiTra) FROM HoaDon hd WHERE hd.ngayTao BETWEEN :startDate AND :endDate")
    Double getTongDoanhThuTheoThang(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT SUM(hd.soTienPhaiTra) FROM HoaDon hd WHERE hd.ngayTao BETWEEN :startDate AND :endDate")
    Double getTongDoanhThuTheoNam(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
