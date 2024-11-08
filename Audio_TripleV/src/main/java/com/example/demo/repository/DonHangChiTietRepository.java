package com.example.demo.repository;

import com.example.demo.entity.DonHangChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DonHangChiTietRepository extends JpaRepository<DonHangChiTiet, Integer> {
//    @Query("select new DonHangChiTietCustom (dhct.id, dh.id, spctc.tenSP, dhct.id, " +
//            "dhct.donGia, dhct.ngayTao, dh.ngayCapNhat) " +
//            "FROM  DonHang dh JOIN DonHangChiTiet dhct " +
//            "ON dh.id = dhct.idDH " +
//            "JOIN SanPhamChiTietCustoms spctc " +
//            "ON dhct.idSPCT = spctc.id")
//    public List<DonHangChiTietCustom> findAllByDonHang();

    public List<DonHangChiTiet> findByDonHang_Id(Integer id);
}
