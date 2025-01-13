package com.example.demo.repository;

import com.example.demo.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Integer> {
    // Tìm kiếm Voucher theo tên, không phân biệt chữ hoa chữ thường
    List<Voucher> findByTenContainingIgnoreCase(String name);
}
