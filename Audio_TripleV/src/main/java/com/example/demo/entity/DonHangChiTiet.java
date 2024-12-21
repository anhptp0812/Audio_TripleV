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

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "DonHangChiTiet")
public class DonHangChiTiet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "ID_Don_Hang", nullable = false)
    private DonHang donHang;

    @ManyToOne
    @JoinColumn(name = "ID_SPCT", nullable = false)
    private SanPhamChiTiet sanPhamChiTiet;

    @Column(name = "SoLuong")
    private Integer soLuong;

    @Column(name = "DonGia")
    private Double donGia;

    @Column(name = "NgayTao")
    private Date ngayTao;

    @Column(name = "NgayCapNhat")
    private Date ngayCapNhat;

    private String formattedDonGia;
    private String formattedThanhTien;

    public void setFormattedDonGia(String formattedDonGia) {
        this.formattedDonGia = formattedDonGia;
    }
    public void setFormattedThanhTien(String formattedThanhTien) {
        this.formattedThanhTien = formattedThanhTien;
    }
}
