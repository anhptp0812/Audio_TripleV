package com.example.demo.service;

import com.example.demo.entity.Voucher;
import com.example.demo.repository.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VoucherService {

    @Autowired
    private VoucherRepository voucherRepository;

    // Lấy tất cả Voucher
    public List<Voucher> getAllVouchers() {
        return voucherRepository.findAll();
    }

    // Lấy Voucher theo ID
    public Optional<Voucher> getVoucherById(Integer id) {
        return voucherRepository.findById(id);
    }

    // Thêm mới hoặc cập nhật Voucher
    public Voucher saveOrUpdateVoucher(Voucher voucher) {
        return voucherRepository.save(voucher);
    }

    // Xóa Voucher theo ID
    public void deleteVoucherById(Integer id) {
        voucherRepository.deleteById(id);
    }
}
