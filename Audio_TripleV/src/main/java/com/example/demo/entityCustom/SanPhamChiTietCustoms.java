package com.example.demo.entityCustom;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class SanPhamChiTietCustoms {

    @Id
    private Integer id;

    private String tenSP;

    private String tenMauSac;

    private String tenHang;

    private String tenHinhAnh;

    private String tenLoaiSP;

    private float donGia;

    private int soLuong;

    private String trangThai;

    private Date ngayTao;

    private Date ngayCapNhat;
}
