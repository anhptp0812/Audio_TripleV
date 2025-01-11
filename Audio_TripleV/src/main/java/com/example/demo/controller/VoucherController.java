package com.example.demo.controller;

import com.example.demo.entity.HoaDon;
import com.example.demo.entity.Voucher;
import com.example.demo.repository.HoaDonRepository;
import com.example.demo.repository.VoucherRepository;
import com.example.demo.service.HoaDonService;
import com.example.demo.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/vouchers")
public class VoucherController {

    @Autowired
    private VoucherService voucherService;

    @Autowired
    private VoucherRepository voucherRepository;

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Autowired
    private HoaDonService hoaDonService;

    // Lấy danh sách tất cả Voucher
    @GetMapping
    public ResponseEntity<List<Voucher>> getAllVouchers() {
        return ResponseEntity.ok(voucherService.getAllVouchers());
    }

    // Lấy Voucher theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Voucher> getVoucherById(@PathVariable Integer id) {
        Optional<Voucher> voucher = voucherService.getVoucherById(id);
        return voucher.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Thêm mới Voucher
    @PostMapping
    public ResponseEntity<Voucher> createVoucher(@RequestBody Voucher voucher) {
        Voucher savedVoucher = voucherService.saveOrUpdateVoucher(voucher);
        return ResponseEntity.ok(savedVoucher);
    }

    // Cập nhật Voucher
    @PutMapping("/{id}")
    public ResponseEntity<Voucher> updateVoucher(@PathVariable Integer id, @RequestBody Voucher voucherDetails) {
        Optional<Voucher> existingVoucher = voucherService.getVoucherById(id);

        if (existingVoucher.isPresent()) {
            Voucher voucher = existingVoucher.get();
            voucher.setTen(voucherDetails.getTen());
            voucher.setLoai(voucherDetails.getLoai());

            // Cập nhật giá trị tùy theo loại Voucher
            if ("GiamTien".equalsIgnoreCase(voucherDetails.getLoai())) {
                voucher.setGiaTriTien(voucherDetails.getGiaTriTien());
                voucher.setGiaTriPhanTram(null); // Đảm bảo giá trị phần trăm bị xóa
            } else if ("GiamPhanTram".equalsIgnoreCase(voucherDetails.getLoai())) {
                voucher.setGiaTriPhanTram(voucherDetails.getGiaTriPhanTram());
                voucher.setGiaTriTien(null); // Đảm bảo giá trị tiền bị xóa
            }

            voucher.setGiaTriHoaDonToiThieu(voucherDetails.getGiaTriHoaDonToiThieu());
            voucher.setTrangThai(voucherDetails.getTrangThai());
            voucher.setNgayBatDau(voucherDetails.getNgayBatDau());
            voucher.setNgayKetThuc(voucherDetails.getNgayKetThuc());
            voucher.setNgayCapNhat(new Date()); // Cập nhật thời gian hiện tại

            return ResponseEntity.ok(voucherService.saveOrUpdateVoucher(voucher));
        }


        return ResponseEntity.notFound().build();
    }

    // Xóa Voucher
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVoucher(@PathVariable Integer id) {
        Optional<Voucher> existingVoucher = voucherService.getVoucherById(id);

        if (existingVoucher.isPresent()) {
            voucherService.deleteVoucherById(id);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping("/apply-voucher")
    public ResponseEntity<Map<String, Object>> applyVoucher(
            @RequestParam Integer hoaDonId,
            @RequestParam Integer voucherId) {

        Optional<HoaDon> hoaDonOptional = hoaDonRepository.findById(hoaDonId);
        if (hoaDonOptional.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Hóa đơn không tồn tại"));
        }

        Optional<Voucher> voucherOptional = voucherRepository.findById(voucherId);
        if (voucherOptional.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Voucher không hợp lệ"));
        }

        HoaDon hoaDon = hoaDonOptional.get();
        Voucher voucher = voucherOptional.get();

        // Kiểm tra nếu tổng tiền nhỏ hơn giá trị tối thiểu của voucher
        if (hoaDon.getTongGia() < voucher.getGiaTriHoaDonToiThieu()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Tổng tiền không đủ để áp dụng voucher"));
        }

        // Tính toán giá trị giảm giá
        double totalPrice = hoaDon.getTongGia();
        double discountedPrice = totalPrice;

        if ("GiamTien".equalsIgnoreCase(voucher.getLoai()) && voucher.getGiaTriTien() > 0) {
            discountedPrice -= voucher.getGiaTriTien();
        } else if ("GiamPhanTram".equalsIgnoreCase(voucher.getLoai()) && voucher.getGiaTriPhanTram() > 0) {
            discountedPrice -= (totalPrice * voucher.getGiaTriPhanTram()) / 100;
        }

        // Đảm bảo giá trị không âm
        discountedPrice = Math.max(discountedPrice, 0);

        // Cập nhật hóa đơn với giá trị mới
        hoaDon.setSoTienPhaiTra(discountedPrice);
        hoaDon.setVouCher(voucher);
        hoaDonRepository.save(hoaDon);

        // Định dạng số tiền đã giảm
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String formattedPrice = currencyFormat.format(discountedPrice);

        return ResponseEntity.ok(Map.of(
                "discountedPrice", formattedPrice,
                "message", "Voucher đã áp dụng thành công"
        ));
    }

}