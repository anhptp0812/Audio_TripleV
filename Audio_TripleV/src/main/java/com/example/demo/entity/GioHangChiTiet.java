package com.example.demo.entity;

import jakarta.persistence.*;
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
    @JoinColumn(name = "ID_GioHang", nullable = false)
    private GioHang gioHang;

    @ManyToOne
    @JoinColumn(name = "ID_SPCT", nullable = false)
    private SanPhamChiTiet sanPhamChiTiet;

    @Column(name = "SoLuong")
    private Integer soLuong;

    @Transient
    public Double getTongGia() {
        // Tính tổng giá cho chi tiết giỏ hàng
        return soLuong * sanPhamChiTiet.getDonGia();
    }
}
