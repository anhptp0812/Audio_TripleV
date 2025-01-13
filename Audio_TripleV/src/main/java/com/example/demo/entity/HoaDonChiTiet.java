package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "HoaDonChiTiet")
public class HoaDonChiTiet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "ID_Hoa_Don")
    private HoaDon hoaDon;

    @ManyToOne
    @JoinColumn(name = "ID_Spct")
    private SanPhamChiTiet sanPhamChiTiet;

    @Column(name = "SoLuong", nullable = false, columnDefinition = "int default 0")
    private Integer soLuong = 0;

    @Column(name = "DonGia")
    private Double donGia;

    @Column(name = "ThoiGianKetThucBH")
    private LocalDate thoiGianKetThucBH;

    @Column(name = "NgayTao")
    private Date ngayTao;

    @Column(name = "NgayCapNhat")
    private Date ngayCapNhat;

    @Transient
    public Double getTongGia() {
        // Tính tổng giá cho chi tiết giỏ hàng
        return soLuong * donGia;
    }

    @Transient
    private String formattedDonGia;

    @Transient
    private String formattedTongGia;

    @Transient
    public String formattedNgayKetThucBaoHanh;

    @Transient
    public String getFormattedNgayKetThucBaoHanh() {
        if (thoiGianKetThucBH != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            return thoiGianKetThucBH.format(formatter);
        }
        return "Không xác định";
    }

}
