package com.example.demo.service;

import com.example.demo.entity.GioHang;
import com.example.demo.entity.GioHangChiTiet;
import com.example.demo.entity.KhachHang;
import com.example.demo.entity.SanPhamChiTiet;
import com.example.demo.repository.GioHangChiTietRepository;
import com.example.demo.repository.GioHangRepository;
import com.example.demo.repository.SanPhamRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
public class GioHangService {

    @Autowired
    private GioHangChiTietRepository gioHangChiTietRepository;

    @Autowired
    private GioHangRepository gioHangRepository;

    @Autowired
    private SanPhamRepository sanPhamRepository;  // Nếu bạn cần truy xuất thông tin sản phẩm

    // Lưu giỏ hàng vào cơ sở dữ liệu
    public GioHang save(GioHang gioHang) {
        return gioHangRepository.save(gioHang);
    }

    // Tìm giỏ hàng của khách hàng
    public Optional<GioHang> findByKhachHang(KhachHang khachHang) {
        return gioHangRepository.findByKhachHang(khachHang);
    }

    // Tạo giỏ hàng mới cho khách hàng
    public GioHang createGioHang(KhachHang khachHang) {
        GioHang gioHang = new GioHang();
        gioHang.setKhachHang(khachHang);
        gioHang.setNgayThem(new Date());
        gioHang.setTrangThai("Mới");  // Thêm trạng thái cho giỏ hàng (ví dụ: "Mới")
        return gioHangRepository.save(gioHang);  // Lưu giỏ hàng mới vào cơ sở dữ liệu
    }

    // Thêm sản phẩm vào giỏ hàng
    @Transactional
    public void themSanPhamVaoGio(GioHang gioHang, SanPhamChiTiet sanPhamChiTiet, Integer soLuong) {
        if (soLuong > sanPhamChiTiet.getSoLuong()) {
            throw new IllegalArgumentException("Số lượng vượt quá tồn kho của sản phẩm.");
        }

        Optional<GioHangChiTiet> existingItem = gioHangChiTietRepository.findByGioHangAndSanPhamChiTiet(gioHang, sanPhamChiTiet);

        if (existingItem.isPresent()) {
            GioHangChiTiet gioHangChiTiet = existingItem.get();
            int newQuantity = gioHangChiTiet.getSoLuong() + soLuong;
            if (newQuantity > sanPhamChiTiet.getSoLuong()) {
                throw new IllegalArgumentException("Tổng số lượng vượt quá tồn kho.");
            }
            gioHangChiTiet.setSoLuong(newQuantity);
            gioHangChiTietRepository.save(gioHangChiTiet);
        } else {
            GioHangChiTiet newGioHangChiTiet = new GioHangChiTiet();
            newGioHangChiTiet.setGioHang(gioHang);
            newGioHangChiTiet.setSanPhamChiTiet(sanPhamChiTiet);
            newGioHangChiTiet.setSoLuong(soLuong);
            gioHangChiTietRepository.save(newGioHangChiTiet);
        }

        updateTongGia(gioHang);
    }

    // Cập nhật tổng giá trị giỏ hàng
    @Transactional
    void updateTongGia(GioHang gioHang) {
        Double tongGia = gioHangChiTietRepository.calculateTotalPrice(gioHang.getId());

        if (!tongGia.equals(gioHang.getTongGia())) {
            gioHang.setTongGia(tongGia);
            gioHangRepository.save(gioHang);
        }
    }

    // Tính tổng giá giỏ hàng (có thể dùng cho các tình huống khác)
    public Double tinhTongGia(GioHang gioHang) {
        double tongGia = 0;
        for (GioHangChiTiet chiTiet : gioHang.getGioHangChiTietList()) {
            tongGia += chiTiet.getSanPhamChiTiet().getDonGia() * chiTiet.getSoLuong();
        }
        return tongGia;
    }
    @Transactional
    public void updateQuantity(Long productId, int quantity) {
        GioHangChiTiet gioHangChiTiet = gioHangChiTietRepository.findBySanPhamChiTiet_Id(productId)
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại trong giỏ hàng"));

        if (quantity <= 0) {
            gioHangChiTietRepository.delete(gioHangChiTiet);
        } else {
            gioHangChiTiet.setSoLuong(quantity);
            gioHangChiTietRepository.save(gioHangChiTiet);
        }

        // Cập nhật tổng giá trị giỏ hàng
        updateTongGia(gioHangChiTiet.getGioHang());
    }

    @Transactional
    public void removeItemFromCart(Long productId) {
        GioHangChiTiet gioHangChiTiet = gioHangChiTietRepository.findBySanPhamChiTiet_Id(productId)
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại trong giỏ hàng"));

        GioHang gioHang = gioHangChiTiet.getGioHang();

        // Xóa khỏi danh sách chi tiết
        gioHang.getGioHangChiTietList().remove(gioHangChiTiet);

        // Xóa khỏi cơ sở dữ liệu
        gioHangChiTietRepository.delete(gioHangChiTiet);

        // Cập nhật tổng giá
        updateTongGia(gioHang);
    }

}