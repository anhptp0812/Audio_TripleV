package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "HoaDon")
public class HoaDon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "ID_Nhan_Vien")
    private NhanVien nhanVien;

    @ManyToOne
    @JoinColumn(name = "ID_Khach_Hang")
    private KhachHang khachHang;

    @ManyToOne
    @JoinColumn(name = "ID_Don_Hang")
    private DonHang donHang;

    @Column(name = "TongGia")
    private Double tongGia;

    @Column(name = "TrangThai")
    private String trangThai;

    @Column(name = "DiaChiGiaoHang")
    private String diaChiGiaoHang;

    @Column(name = "NgayGiao")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date ngayGiao;

    @Column(name = "NgayTao")
    private Date ngayTao;

    @Column(name = "NgayCapNhat")
    private Date ngayCapNhat;
}
