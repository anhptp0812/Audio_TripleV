package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "HoaDonChiTiet")
public class HoaDonChiTiet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "ID_Hoa_Don")
    private HoaDon hoaDon;

    @ManyToOne
    @JoinColumn(name = "ID_Spct")
    private SanPhamChiTiet sanPhamChiTiet;

    @Column(name = "SoLuong", nullable = false, columnDefinition = "int default 0")
    private Integer soLuong = 0;

    @Column(name = "DonGia")
    private Double donGia;

    @Column(name = "NgayTao")
    private Date ngayTao;

    @Column(name = "NgayCapNhat")
    private Date ngayCapNhat;
}
