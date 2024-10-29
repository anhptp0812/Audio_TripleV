package com.example.demo.entityCustom;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class DonHangChiTietCustom {
    @Id
    private Integer id;

    private Integer idDonHang;

    private String tenSP;

    private Integer soLuong;

    private Double donGia;

    private Date ngayTao;

    private Date ngayCapNhat;
}
