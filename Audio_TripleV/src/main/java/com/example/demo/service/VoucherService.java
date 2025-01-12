package com.example.demo.service;

import com.example.demo.entity.Voucher;
import com.example.demo.repository.VoucherRepository;
import jakarta.persistence.EntityNotFoundException;
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

    public Voucher getVoucherById1(Integer id) {
        return voucherRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy bài viết với ID: " + id));
    }

    // Thêm mới hoặc cập nhật Voucher
    public Voucher saveOrUpdateVoucher(Voucher voucher) {
        return voucherRepository.save(voucher);
    }

    // Xóa Voucher theo ID
    public void deleteVoucherById(Integer id) {
        voucherRepository.deleteById(id);
    }

    // Lưu một voucher mới
    public Voucher save(Voucher voucher) {
        return voucherRepository.save(voucher);
    }

    // Lấy tất cả voucher
    public List<Voucher> findAll() {
        return voucherRepository.findAll();
    }

    // Lấy voucher theo id
    public Voucher findById(Integer id) {
        Optional<Voucher> voucher = voucherRepository.findById(id);
        return voucher.orElse(null);  // Trả về null nếu không tìm thấy
    }

    // Cập nhật thông tin voucher
    public Voucher updateVoucher(Voucher voucher) {
        if (voucher.getId() != null && voucherRepository.existsById(voucher.getId())) {
            return voucherRepository.save(voucher);
        } else {
            throw new IllegalArgumentException("Voucher không tồn tại!");
        }
    }

    // Xóa voucher theo id
    public void deleteById(Integer id) {
        voucherRepository.deleteById(id);
    }
}
