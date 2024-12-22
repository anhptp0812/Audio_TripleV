package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
import lombok.ToString;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;
import java.util.Locale;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "SanPhamChiTiet")
public class SanPhamChiTiet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_San_Pham")
    private SanPham sanPham;

    @ManyToOne
    @JoinColumn(name = "ID_MauSac")
    private MauSac mauSac;

    @ManyToOne
    @JoinColumn(name = "ID_Hang")
    private Hang hang;

    @ManyToOne
    @JoinColumn(name = "ID_HinhAnh")
    private HinhAnh hinhAnh;

    @ManyToOne
    @JoinColumn(name = "ID_LoaiSanPham")
    private LoaiSanPham loaiSanPham;

    @Column(name = "DonGia")
    private double donGia;

    @Column(name = "soLuong")
    private int soLuong;

    @Column(name = "TrangThai")
    private String trangThai;

    @Column(name = "MoTa", columnDefinition = "NVARCHAR(MAX)")
    private String moTa;

    @Column(name = "NgayTao")
    private Date ngayTao;

    @Column(name = "NgayCapNhat")
    private Date ngayCapNhat;

    private String formattedDonGia;

    public SanPhamChiTiet(Integer id) {
        this.id = id;
    }

    public String getFormattedDonGia() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.GERMANY);
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');

        DecimalFormat decimalFormat = new DecimalFormat("#,###", symbols);
        return decimalFormat.format(donGia);
    }

    public void setFormattedDonGia(String formattedDonGia) {
        this.formattedDonGia = formattedDonGia;

    }
}
