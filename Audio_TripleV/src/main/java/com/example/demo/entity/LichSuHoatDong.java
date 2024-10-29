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
@Table(name = "LichSuHoatDong")
public class LichSuHoatDong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "ID_NhanVien")
    private NhanVien nhanVien;

    @Column(name = "ThoiGian")
    private Date thoiGian;

    @Column(name = "MoTa")
    private String moTa;

    @Column(name = "TrangThai")
    private String trangThai;

    @Column(name = "NgayTao")
    private Date ngayTao;

    @Column(name = "NgayCapNhat")
    private Date ngayCapNhat;

    @Column(name = "NguoiTao")
    private String nguoiTao;

    @Column(name = "NguoiCapNhat")
    private String nguoiCapNhat;
}
