package com.example.demo.service;

import com.example.demo.entity.GioHang;
import com.example.demo.entity.GioHangChiTiet;
import com.example.demo.entity.HoaDon;
import com.example.demo.entity.HoaDonChiTiet;
import com.example.demo.entity.SanPhamChiTiet;
import com.example.demo.repository.HoaDonChiTietRepository;
import com.example.demo.repository.HoaDonRepository;
import com.example.demo.repository.SanPhamChiTietRepository;
import org.hibernate.query.sql.internal.ParameterRecognizerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class HoaDonService {

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Autowired
    private HoaDonChiTietRepository hoaDonChiTietRepository;

    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;
    public HoaDon findByid(Integer id) {
        return hoaDonRepository.findById(id).orElse(null);
    }

    public List<HoaDonChiTiet> findByDHid(Integer id) {
        return hoaDonChiTietRepository.findByHoaDon_Id(id);
    }

    @Transactional
    public void updateTongGia(HoaDon hoaDon) {
        // Kiểm tra nếu giỏ hàng không trống
        if (hoaDon.getHoaDonChiTietList() == null) {
            hoaDon.setTongGia(0.0); // Nếu giỏ hàng trống, set tổng giá là 0
        } else {
            // Tính tổng giá từ các sản phẩm còn lại trong giỏ hàng
            Double tongGia = hoaDonChiTietRepository.calculateTotalPrice(hoaDon.getId());

            // Nếu tổng giá tính ra là null, thì gán về 0.0
            if (tongGia == null) {
                tongGia = 0.0;
            }

            hoaDon.setTongGia(tongGia); // Cập nhật tổng giá
        }

        hoaDonRepository.save(hoaDon);
    }

    public void themSanPhamVaoGio(HoaDon hoaDon, SanPhamChiTiet sanPhamChiTiet, Integer soLuong) {  if (soLuong > sanPhamChiTiet.getSoLuong()) {
        throw new IllegalArgumentException("Số lượng vượt quá tồn kho của sản phẩm.");
    }

        Optional<HoaDonChiTiet> existingItem = hoaDonChiTietRepository.findByHoaDonAndSanPhamChiTiet(hoaDon, sanPhamChiTiet);

        if (existingItem.isPresent()) {
            HoaDonChiTiet hoaDonChiTiet = existingItem.get();
            int newQuantity = hoaDonChiTiet.getSoLuong() + soLuong;
            if (newQuantity > sanPhamChiTiet.getSoLuong()) {
                throw new IllegalArgumentException("Tổng số lượng vượt quá tồn kho.");
            }
            hoaDonChiTiet.setSoLuong(newQuantity);
            hoaDonChiTietRepository.save(hoaDonChiTiet);
        } else {
            HoaDonChiTiet newHoaDonChiTiet = new HoaDonChiTiet();
            newHoaDonChiTiet.setHoaDon(hoaDon);
            newHoaDonChiTiet.setSanPhamChiTiet(sanPhamChiTiet);
            newHoaDonChiTiet.setDonGia(sanPhamChiTiet.getDonGia());
            newHoaDonChiTiet.setSoLuong(soLuong);
            newHoaDonChiTiet.setNgayTao(new Date());
            hoaDonChiTietRepository.save(newHoaDonChiTiet);
        }
    }

    @Transactional
    public void updateQuantity(Integer productId, int quantity) {
        HoaDonChiTiet hoaDonChiTiet = hoaDonChiTietRepository.findBySanPhamChiTiet_Id(productId)
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại trong giỏ hàng"));

        if (quantity <= 0) {
            hoaDonChiTietRepository.delete(hoaDonChiTiet);
        } else {
            hoaDonChiTiet.setSoLuong(quantity);
            hoaDonChiTietRepository.save(hoaDonChiTiet);
        }
    }

    public void removeProductFromCart(Integer productId) {
        // Tìm HoaDonChiTiet theo productId
        HoaDonChiTiet hoaDonChiTiet = findHoaDonChiTietByProductId(productId);

        // Hoàn lại số lượng vào kho
        SanPhamChiTiet sanPhamChiTiet = hoaDonChiTiet.getSanPhamChiTiet();
        sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() + hoaDonChiTiet.getSoLuong());
        sanPhamChiTietRepository.save(sanPhamChiTiet);

        // Lấy hóa đơn liên quan
        HoaDon hoaDon = hoaDonChiTiet.getHoaDon();

        // Cập nhật tổng giá trị hóa đơn
        double newTotal = (hoaDon.getTongGia() != null ? hoaDon.getTongGia() : 0)
                - hoaDonChiTiet.getDonGia() * hoaDonChiTiet.getSoLuong();
        hoaDon.setTongGia(Math.max(newTotal, 0)); // Đảm bảo không âm

        // Xóa HoaDonChiTiet khỏi danh sách
        hoaDon.getHoaDonChiTietList().remove(hoaDonChiTiet);

        // Nếu giỏ hàng trống, cập nhật trạng thái hóa đơn
        if (hoaDon.getHoaDonChiTietList().isEmpty()) {
            hoaDon.setTrangThai("TRỐNG");
        }

        // Lưu hóa đơn cập nhật
        hoaDonRepository.save(hoaDon);

        // Xóa HoaDonChiTiet khỏi cơ sở dữ liệu
        hoaDonChiTietRepository.delete(hoaDonChiTiet);
    }

    public HoaDonChiTiet findHoaDonChiTietByProductId(Integer productId) {
        // Tìm kiếm HoaDonChiTiet theo productId
        return hoaDonChiTietRepository.findBySanPhamChiTiet_Id(productId)
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại trong giỏ hàng."));
    }
}
