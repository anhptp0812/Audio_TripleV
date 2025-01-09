package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ThongKe {

    private String thoiGian; // Ngày, tháng hoặc năm

    private int soLuongDonHang;

    private double tongDoanhThu;

    private double loiNhuan;
}
