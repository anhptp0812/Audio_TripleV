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
@Table(name = "SanPhamChiTiet_KhuyenMai")
public class SanPhamChiTietKhuyenMai {
    @Id
    @ManyToOne
    @JoinColumn(name = "ID_Spct")
    private SanPhamChiTiet sanPhamChiTiet;

    @Id
    @ManyToOne
    @JoinColumn(name = "ID_Khuyen_Mai")
    private KhuyenMai khuyenMai;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
}
