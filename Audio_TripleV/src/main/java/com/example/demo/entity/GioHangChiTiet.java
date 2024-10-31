package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "GioHangChiTiet")
public class GioHangChiTiet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "ID_GioHang")
    private GioHang gioHang;

    @ManyToOne
    @JoinColumn(name = "ID_SPCT")
    private SanPhamChiTiet sanPhamChiTiet;

    @Column(name = "SoLuong")
    private Integer soLuong;

    // Phương thức tính toán tổng giá cho sản phẩm trong giỏ hàng
    public Double tinhTongGia() {
        if (sanPhamChiTiet != null && soLuong != null) {
            return sanPhamChiTiet.getDonGia() * soLuong; // Giả sử sanPhamChiTiet có thuộc tính donGia
        }
        return 0.0;
    }
}
