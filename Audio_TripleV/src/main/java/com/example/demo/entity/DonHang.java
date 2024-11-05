package com.example.demo.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "DonHang")
public class DonHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "ID_Khach_Hang")
    private KhachHang khachHang;

    @Column(name = "TongGia")
    private Double tongGia;

    @Column(name = "TrangThai")
    private String trangThai;

    @Column(name = "NgayGiao")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date ngayGiao;

    @Column(name = "NgayTao")
    private Date ngayTao;

    @Column(name = "NgayCapNhat")
    private Date ngayCapNhat;


}
