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
import java.util.List;
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
//    @Transactional
//    public void updateTongGia(GioHang gioHang) {
//        // Tính tổng giá từ repository hoặc tính toán trong service
//        Double tongGia = gioHangChiTietRepository.calculateTotalPrice(gioHang.getId());
//
//        // Kiểm tra xem tổng giá đã thay đổi chưa, nếu thay đổi thì cập nhật
//        if (!tongGia.equals(gioHang.getTongGia())) {
//            gioHang.setTongGia(tongGia);
//            gioHangRepository.save(gioHang);
//        }
//    }

    @Transactional
    public void updateTongGia(GioHang gioHang) {
        // Kiểm tra nếu giỏ hàng không trống
        if (gioHang.getGioHangChiTietList() == null) {
            gioHang.setTongGia(0.0); // Nếu giỏ hàng trống, set tổng giá là 0
        } else {
            // Tính tổng giá từ các sản phẩm còn lại trong giỏ hàng
            Double tongGia = gioHangChiTietRepository.calculateTotalPrice(gioHang.getId());

            // Nếu tổng giá tính ra là null, thì gán về 0.0
            if (tongGia == null) {
                tongGia = 0.0;
            }

            gioHang.setTongGia(tongGia); // Cập nhật tổng giá
        }

        gioHangRepository.save(gioHang);
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

    /**
     * Xóa một sản phẩm khỏi giỏ hàng của khách hàng.
     */

    @Transactional
    public void removeSanPham(KhachHang khachHang, Integer sanPhamChiTietId) {
        // Tìm giỏ hàng của khách hàng
        GioHang gioHang = gioHangRepository.findByKhachHang(khachHang)
                .orElseThrow(() -> new RuntimeException("Giỏ hàng không tồn tại"));

        List<GioHangChiTiet> gioHangChiTietList = gioHang.getGioHangChiTietList();

        // Tìm sản phẩm cần xóa
        GioHangChiTiet chiTiet = gioHangChiTietList.stream()
                .filter(item -> item.getSanPhamChiTiet().getId().equals(sanPhamChiTietId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại trong giỏ hàng"));

        // Xóa chi tiết sản phẩm khỏi giỏ hàng
        gioHangChiTietList.remove(chiTiet);

        // Cập nhật danh sách trong database
        gioHang.setGioHangChiTietList(gioHangChiTietList);
        gioHangChiTietRepository.delete(chiTiet);
        gioHangRepository.save(gioHang);

        // Cập nhật tổng giá giỏ hàng sau khi xóa sản phẩm
        updateTongGia(gioHang); // Gọi lại phương thức tính tổng giá giỏ hàng
    }
}