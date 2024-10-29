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
@Table(name = "DonHangChiTiet")
public class DonHangChiTiet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

//    @ManyToOne
//    @JoinColumn(name = "ID_Don_Hang")
//    private DonHang donHang;

    @Column(name = "ID_Don_Hang")
    private Integer idDH;

//    @ManyToOne
//    @JoinColumn(name = "ID_SPCT")  // Reference to SanPhamChiTiet
//    private SanPhamChiTiet sanPhamChiTiet;


    @Column(name = "ID_SPCT")  // Reference to SanPhamChiTiet
    private Integer idSPCT;

    @Column(name = "SoLuong")
    private Integer soLuong;

    @Column(name = "DonGia")
    private Double donGia;

    @Column(name = "NgayTao")
    private Date ngayTao;

    @Column(name = "NgayCapNhat")
    private Date ngayCapNhat;
}
