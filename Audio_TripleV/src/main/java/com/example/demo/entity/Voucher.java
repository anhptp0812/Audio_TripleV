package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Voucher")
public class Voucher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "Ten")
    private String ten;

    @Column(name = "Loai")
    private String loai;

    @Column(name = "GiaTriTien")
    private Double giaTriTien;

    @Column(name = "GiaTriPhanTram")
    private Double giaTriPhanTram;

    @Column(name = "GiaTriHoaDonToiThieu")
    private Double giaTriHoaDonToiThieu;

    @Column(name = "TrangThai")
    private String trangThai;

    @Column(name = "NgayBatDau")
    private Date ngayBatDau;

    @Column(name = "NgayKetThuc")
    private Date ngayKetThuc;

    @Column(name = "NgayTao")
    private Date ngayTao;

    @Column(name = "NgayCapNhat")
    private Date ngayCapNhat;

    private String formattedGiaTriTien;
    private String formattedGiaTriPhanTram;
    private String formattedGiaTriHoaDonToiThieu;

    public String getValueForThymeleaf() {
        if (giaTriTien != null) {
            return String.format("%.0f", giaTriTien); // Định dạng số không có phần thập phân
        } else if (giaTriPhanTram != null) {
            return String.format("%.0f", giaTriPhanTram); // Định dạng số không có phần thập phân
        }
        return "";
    }

    public String getValueMinForThymeleaf() {
        if (giaTriHoaDonToiThieu != null) {
            return String.format("%.0f", giaTriHoaDonToiThieu); // Định dạng số không có phần thập phân
        }
        return "";
    }
}
