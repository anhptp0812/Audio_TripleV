package com.example.demo.repository;

import com.example.demo.entity.SanPhamChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
    public interface SanPhamChiTietRepository extends JpaRepository<SanPhamChiTiet, Integer> {
//        @Query("SELECT NEW SanPhamChiTietCustom (spct.id, sp.tenSP, ms.ten, h.ten, ha.tenUrl, lsp.kieuDang, " +
//                "spct.donGia, spct.soLuong, spct.trangThai, spct.ngayTao, spct.ngayCapNhat )" +
//                "FROM SanPhamChiTiet spct JOIN SanPham sp " +
//                "ON spct.id = sp.id JOIN LoaiSanPham lsp " +
//                "ON spct.idLoaiSP = lsp.id JOIN MauSac ms " +
//                "ON spct.idMauSac = ms.id JOIN Hang h " +
//                "ON spct.idHang = h.id JOIN HinhAnh ha " +
//                "ON spct.idHinhAnh = ha.id  " +
//                "WHERE spct.idLoaiSP = 5")
//        List<SanPhamChiTietCustom> findSanPhamChiTietByIdLoaiSP(Integer idLoaiSP);


}
