package com.example.demo.service;

import com.example.demo.entity.KhachHang;
import com.example.demo.repository.KhachHangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class KhachHangService {

    @Autowired
    private KhachHangRepository khachHangRepository;

    // Lưu hoặc cập nhật thông tin khách hàng
    @Transactional
    public KhachHang save(KhachHang khachHang) {
        return khachHangRepository.save(khachHang);
    }

    // Tìm khách hàng theo ID
    public Optional<KhachHang> findById(Integer id) {
        return khachHangRepository.findById(id);
    }

    // Tìm khách hàng theo tài khoản
    public Optional<KhachHang> findByTaiKhoan(String taiKhoan) {
        return khachHangRepository.findByTaiKhoan(taiKhoan);
    }

    // Xóa khách hàng theo ID
    @Transactional
    public void deleteById(Integer id) {
        khachHangRepository.deleteById(id);
    }

    // Tìm kiếm khách hàng theo tên và số điện thoại
    public List<KhachHang> searchByNameAndPhone(String name, String phone) {
        if (name != null && phone != null) {
            return khachHangRepository.findByTenContainingAndSdtContaining(name, phone);
        } else if (name != null) {
            return khachHangRepository.findByTenContaining(name);
        } else if (phone != null) {
            return khachHangRepository.findBySdtContaining(phone);
        } else {
            return List.of(); // Nếu không có điều kiện tìm kiếm nào, trả về danh sách rỗng
        }
    }

    // Tạo mới khách hàng nếu chưa tồn tại
    public KhachHang createCustomer(String name, String phone, String address) {
        // Kiểm tra xem khách hàng đã tồn tại chưa
        Optional<KhachHang> existingCustomer = khachHangRepository.findBySdt(phone);
        if (existingCustomer.isPresent()) {
            throw new RuntimeException("Khách hàng với số điện thoại này đã tồn tại.");
        }

        KhachHang khachHang = new KhachHang();
        khachHang.setTen(name);
        khachHang.setSdt(phone);
        khachHang.setDiaChi(address);
        khachHang.setIsRegistered(false); // Đánh dấu là khách hàng chưa đăng ký
        khachHang.setNgayDangKy(new Date());
        return khachHangRepository.save(khachHang);
    }

    // Lấy khách hàng theo ID và ném ngoại lệ nếu không tìm thấy
    public KhachHang getCustomerById(Integer id) {
        return khachHangRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Khách hàng không tồn tại"));
    }
    public Optional<KhachHang> findCustomerByPhone(String phone) {
        return khachHangRepository.findBySdt(phone);
    }

}


