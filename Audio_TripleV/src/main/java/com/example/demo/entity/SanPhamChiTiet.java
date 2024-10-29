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
@Table(name = "SanPhamChiTiet")
public class SanPhamChiTiet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @ManyToOne
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

//    @Column(name = "ID_San_Pham")
//    private Integer idSp;
////
//    @Column(name = "ID_MauSac")
//    private Integer idMauSac;
//
//    @Column(name = "ID_Hang")
//    private Integer idHang;
//
//    @Column(name = "ID_HinhAnh")
//    private Integer idHinhAnh;
//
//    @Column(name = "ID_LoaiSanPham")
//    private Integer idLoaiSP;

    @Column(name = "DonGia")
    private float donGia;

    @Column(name = "soLuong")
    private int soLuong;

    @Column(name = "TrangThai")
    private String trangThai;

    @Column(name = "NgayTao")
    private Date ngayTao;

    @Column(name = "NgayCapNhat")
    private Date ngayCapNhat;
}
