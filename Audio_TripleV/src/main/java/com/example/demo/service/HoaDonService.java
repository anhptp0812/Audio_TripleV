package com.example.demo.service;

import com.example.demo.entity.GioHang;
import com.example.demo.entity.GioHangChiTiet;
import com.example.demo.entity.HoaDon;
import com.example.demo.entity.HoaDonChiTiet;
import com.example.demo.entity.SanPhamChiTiet;
import com.example.demo.entity.Voucher;
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
        if (hoaDon.getHoaDonChiTietList() == null || hoaDon.getHoaDonChiTietList().isEmpty()) {
            hoaDon.setTongGia(0.0); // Nếu giỏ hàng trống, set tổng giá là 0
        } else {
            // Tính tổng giá từ các sản phẩm còn lại trong giỏ hàng
            double total = hoaDon.getHoaDonChiTietList().stream()
                    .mapToDouble(item -> item.getSoLuong() * item.getDonGia())
                    .sum();

            // Nếu tổng giá tính ra là null hoặc tổng giá âm, gán về 0.0
            hoaDon.setTongGia(Math.max(total, 0)); // Đảm bảo không âm
            hoaDon.setSoTienPhaiTra(hoaDon.getTongGia());
        }

        hoaDonRepository.save(hoaDon); // Lưu lại hóa đơn với tổng giá đã cập nhật
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

        SanPhamChiTiet sanPhamChiTiet = hoaDonChiTiet.getSanPhamChiTiet();
        int oldQuantity = hoaDonChiTiet.getSoLuong();
        double unitPrice = hoaDonChiTiet.getDonGia();
        double oldTotal = oldQuantity * unitPrice;

        // Cập nhật số lượng trong kho
        int deltaQuantity = quantity - oldQuantity;
        sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() - deltaQuantity);
        sanPhamChiTietRepository.save(sanPhamChiTiet);

        // Cập nhật số lượng trong giỏ hoặc xóa nếu số lượng bằng 0
        if (quantity <= 0) {
            hoaDonChiTietRepository.delete(hoaDonChiTiet);
        } else {
            hoaDonChiTiet.setSoLuong(quantity);
            hoaDonChiTietRepository.save(hoaDonChiTiet);
        }

        // Cập nhật tổng giá
        HoaDon hoaDon = hoaDonChiTiet.getHoaDon();
        double newTotal = (hoaDon.getTongGia() != null ? hoaDon.getTongGia() : 0) - oldTotal + (quantity * unitPrice);
        hoaDon.setTongGia(Math.max(newTotal, 0));
        updateTongGiaWithVoucher(hoaDon);
    }

    public void removeProductFromCart(Integer productId) {
        HoaDonChiTiet hoaDonChiTiet = findHoaDonChiTietByProductId(productId);

        SanPhamChiTiet sanPhamChiTiet = hoaDonChiTiet.getSanPhamChiTiet();
        sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() + hoaDonChiTiet.getSoLuong());
        sanPhamChiTietRepository.save(sanPhamChiTiet);

        HoaDon hoaDon = hoaDonChiTiet.getHoaDon();

        double newTotal = (hoaDon.getTongGia() != null ? hoaDon.getTongGia() : 0)
                - hoaDonChiTiet.getDonGia() * hoaDonChiTiet.getSoLuong();
        hoaDon.setTongGia(Math.max(newTotal, 0)); // Đảm bảo không âm

        hoaDon.getHoaDonChiTietList().remove(hoaDonChiTiet);

        if (hoaDon.getHoaDonChiTietList().isEmpty()) {
            hoaDon.setTrangThai("Chưa thanh toán");
        }

        hoaDonRepository.save(hoaDon);
        hoaDonChiTietRepository.delete(hoaDonChiTiet);
    }

    public HoaDonChiTiet findHoaDonChiTietByProductId(Integer productId) {
        // Tìm kiếm HoaDonChiTiet theo productId
        return hoaDonChiTietRepository.findBySanPhamChiTiet_Id(productId)
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại trong giỏ hàng."));
    }

    public HoaDon findById(Integer id) {
        return hoaDonRepository.findById(id).orElse(null);
    }

    @Transactional
    public void updateQuantityInHoaDon(Integer productId, int quantity) {
        HoaDonChiTiet hoaDonChiTiet = hoaDonChiTietRepository.findBySanPhamChiTiet_Id(productId)
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại trong hóa đơn"));

        if (quantity <= 0) {
            hoaDonChiTietRepository.delete(hoaDonChiTiet);
        } else {
            hoaDonChiTiet.setSoLuong(quantity);
            hoaDonChiTietRepository.save(hoaDonChiTiet);
        }

        // Cập nhật tổng giá trị hóa đơn (nếu cần thiết)
        updateTongGia(hoaDonChiTiet.getHoaDon());
    }

    @Transactional
    public void updateTongGiaWithVoucher(HoaDon hoaDon) {
        // Kiểm tra nếu giỏ hàng không trống
        if (hoaDon.getHoaDonChiTietList() == null || hoaDon.getHoaDonChiTietList().isEmpty()) {
            hoaDon.setTongGia(0.0); // Nếu giỏ hàng trống, set tổng giá là 0
            hoaDon.setSoTienPhaiTra(0.0); // Nếu giỏ hàng trống, số tiền phải trả là 0
        } else {
            // Tính tổng giá từ các sản phẩm còn lại trong giỏ hàng
            double total = hoaDon.getHoaDonChiTietList().stream()
                    .mapToDouble(item -> item.getSoLuong() * item.getDonGia())
                    .sum();

            // Áp dụng voucher nếu có
            double discountedPrice = total;
            if (hoaDon.getVouCher() != null) {
                Voucher voucher = hoaDon.getVouCher();

                if ("GiamTien".equalsIgnoreCase(voucher.getLoai()) && voucher.getGiaTriTien() > 0) {
                    discountedPrice = Math.max(total - voucher.getGiaTriTien(), 0);
                } else if ("GiamPhanTram".equalsIgnoreCase(voucher.getLoai()) && voucher.getGiaTriPhanTram() > 0) {
                    discountedPrice = Math.max(total - (total * voucher.getGiaTriPhanTram() / 100), 0);
                }

                if(total < hoaDon.getVouCher().getGiaTriHoaDonToiThieu()){
                    hoaDon.setVouCher(null);
                    discountedPrice = total;
                }
            }

            // Cập nhật tổng giá và số tiền phải trả
            hoaDon.setTongGia(total);
            hoaDon.setSoTienPhaiTra(discountedPrice);
        }

        hoaDonRepository.save(hoaDon); // Lưu lại hóa đơn với tổng giá đã cập nhật
    }

}
