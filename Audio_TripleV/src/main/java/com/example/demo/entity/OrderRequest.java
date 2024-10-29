package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private int customerId;         // ID khách hàng
    private double totalAmount;     // Tổng giá trị đơn hàng
    private Date orderDate;         // Ngày tạo đơn hàng
    private List<OrderProduct> products;  // Danh sách các sản phẩm trong đơn hàng

}
