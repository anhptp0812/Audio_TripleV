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

public class SanPhamChiTietCustom {

    @Id
    private Integer id;


    private String tenSanPham;

    private String tenMauSac;


    private String tenHang;

    private String tenHinhAnh;


    private String tenLoaiSanPham;

    private float donGia;

    private String trangThai;


    private Date ngayTao;

    private Date ngayCapNhat;

}
