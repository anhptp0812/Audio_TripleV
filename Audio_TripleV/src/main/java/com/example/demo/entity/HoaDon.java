package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

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
    @JoinColumn(name = "ID_Voucher")
    private Voucher voucher;

    @Column(name = "TongGia")
    private Double tongGia = 0.0;  // Đảm bảo có giá trị mặc định nếu null

    @Column(name = "TienKhachDua")
    private Double tienKhachDua = 0.0; // Giá trị mặc định

    @Column(name = "TienThua")
    private Double tienThua = 0.0; // Giá trị mặc định

    @Column(name = "TrangThai")
    private String trangThai;

    @Column(name = "NgayTao")
    private Date ngayTao;

    @Column(name = "NgayCapNhat")
    private Date ngayCapNhat;

    private String formattedTongGia;

    private String formattedTienKhachDua;

    private String formattedTienThua;

    @OneToMany(mappedBy = "hoaDon", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<HoaDonChiTiet> hoaDonChiTietList;



}
