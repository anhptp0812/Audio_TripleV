package com.example.demo.entity;

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
@Table(name = "GioHang")
public class GioHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "ID_Khach_Hang", nullable = false)
    private KhachHang khachHang;

    @Column(name = "TongGia")
    private Double tongGia = 0.0;

    @Column(name = "NgayThem")
    private Date ngayThem;

    @Column(name = "TrangThai")
    private String trangThai;

    @OneToMany(mappedBy = "gioHang", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<GioHangChiTiet> gioHangChiTietList;
}
