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
    // Phương thức tìm kiếm tất cả sản phẩm theo các bộ lọc
    @Query("SELECT s FROM SanPhamChiTiet s WHERE " +
            "(:idLoaiSP IS NULL OR s.loaiSanPham.id = :idLoaiSP) AND " +
            "(:idSanPham IS NULL OR s.sanPham.id = :idSanPham) AND " +
            "(:mauSac IS NULL OR s.mauSac.id = :mauSac) AND " +
            "(:hang IS NULL OR s.hang.id = :hang) AND " +
            "(:minPrice IS NULL OR s.donGia >= :minPrice) AND " +
            "(:maxPrice IS NULL OR s.donGia <= :maxPrice)")
    List<SanPhamChiTiet> findByFilters(Integer idLoaiSP, Integer idSanPham, Integer mauSac, Integer hang,
                                       Double minPrice, Double maxPrice);

    @Query("SELECT s FROM SanPhamChiTiet s ORDER BY s.ngayTao DESC")
    List<SanPhamChiTiet> findTop4NewestProducts(Pageable pageable);
    List<SanPhamChiTiet> findTop4ByOrderByIdDesc();
}
