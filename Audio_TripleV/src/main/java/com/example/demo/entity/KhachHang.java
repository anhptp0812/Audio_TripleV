package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "KhachHang")
public class KhachHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "Ten")
    private String ten;

    @Column(name = "sdt")
    private String sdt;

    @Column(name = "DiaChi")
    private String diaChi;

    @Column(name = "Email")
    private String email;

    @Column(name = "TaiKhoan")
    private String taiKhoan;

    @Column(name = "MatKhau")
    private String matKhau;

    @Column(name = "role")
    private String role;

    @Column(name = "isRegitered")
    private Boolean isRegistered;

    @Column(name = "NgayDangKy")
    private Date ngayDangKy;

    @OneToOne(mappedBy = "khachHang", cascade = CascadeType.ALL)
    @JsonIgnore
    private GioHang gioHang;

    @OneToMany(mappedBy = "khachHang") // Quan hệ 1-N với bảng comments
    private List<Comment> comments;
}
