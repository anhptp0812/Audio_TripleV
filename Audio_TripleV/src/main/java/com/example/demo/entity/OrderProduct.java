package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderProduct {
    private int sanPhamChiTietId;  // ID sản phẩm chi tiết
    private int quantity;          // Số lượng sản phẩm
    private double price;
}
