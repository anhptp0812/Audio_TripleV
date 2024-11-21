package com.example.demo.entity;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class QuickBuyRequest {


    private Integer sanPhamChiTietId; // ID sản phẩm chi tiết
    private Integer khachHangId; // Nếu người dùng đã đăng nhập
    private String tenKhachHang; // Nếu chưa đăng nhập
    private String sdt; // Nếu chưa đăng nhập
    private String diaChi; // Nếu chưa đăng nhập
    private Integer soLuong; // Số lượng sản phẩm
}
