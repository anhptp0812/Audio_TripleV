package com.example.demo.api;


import com.example.demo.entity.DonHang;
import com.example.demo.entity.HoaDon;
import com.example.demo.entity.HoaDonChiTiet;
import com.example.demo.entity.KhachHang;
import com.example.demo.entity.NhanVien;
import com.example.demo.entity.QuickBuyRequest;
import com.example.demo.entity.SanPhamChiTiet;

import com.example.demo.entity.HoaDon;
import com.example.demo.entity.HoaDonChiTiet;
import com.example.demo.repository.DonHangRepository;
import com.example.demo.repository.DonHangChiTietRepository;
import com.example.demo.repository.HangRepository;
import com.example.demo.repository.HoaDonChiTietRepository;

import com.example.demo.repository.HoaDonRepository;
import com.example.demo.repository.NhanVienRepo;
import com.example.demo.repository.SanPhamChiTietRepository;
import com.example.demo.service.DonHangService;
import com.example.demo.service.KhachHangService;
import com.example.demo.service.NhanVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class HoaDonController {
    @Autowired
    private DonHangService donHangService;

    @Autowired
    private NhanVienRepo nhanVienRepo;
    @Autowired
    SanPhamChiTietRepository sanPhamChiTietRepository;

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Autowired
    private KhachHangService khachHangService;

    @Autowired
    private NhanVienService nhanVienService;

    @PostMapping("/ban-hang/save")
    public ResponseEntity<Map<String, String>> saveDonHang(@ModelAttribute("donHang") HoaDon hoaDon) {
        // Lấy ID của nhân viên đang đăng nhập
        Integer nhanVienId = nhanVienService.getLoggedInNhanVienId();

        // Kiểm tra nếu không có ID nhân viên
        if (nhanVienId == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Chưa đăng nhập"));
        }

        // Lấy nhân viên từ ID
        Optional<NhanVien> nhanVienOptional = nhanVienRepo.findById(nhanVienId);
        if (nhanVienOptional.isPresent()) {
            NhanVien nhanVien = nhanVienOptional.get();

            // Gán nhân viên vào hóa đơn
            hoaDon.setNhanVien(nhanVien);

            // Kiểm tra và thiết lập mặc định nếu trạng thái không có
            if (hoaDon.getTrangThai() == null || hoaDon.getTrangThai().isEmpty()) {
                hoaDon.setTrangThai("Chưa thanh toán"); // Thiết lập mặc định
            }

            // Kiểm tra và thiết lập mặc định nếu tổng giá không có
            if (hoaDon.getTongGia() == null) {
                hoaDon.setTongGia(0.0); // Thiết lập tổng giá mặc định
            }

            hoaDon.setNgayTao(new Date());
            hoaDon.setNgayCapNhat(new Date());

            // Lưu hóa đơn vào cơ sở dữ liệu
            hoaDonRepository.save(hoaDon);

            // Trả về URL của trang đích sau khi lưu thành công
            Map<String, String> response = new HashMap<>();
            response.put("redirectUrl", "/user/ban-hang/" + hoaDon.getId());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Không tìm thấy nhân viên"));
        }
    }


    @PostMapping("/orders/quick-buy")
    public ResponseEntity<?> quickBuy(@RequestBody QuickBuyRequest request) {
        try {
            // Kiểm tra ID sản phẩm có null không
            if (request.getSanPhamChiTietId() == null) {
                return ResponseEntity.badRequest().body(Map.of("message", "ID sản phẩm không thể để trống"));
            }

            // Tìm kiếm sản phẩm trong cơ sở dữ liệu
            SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(request.getSanPhamChiTietId())
                    .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

            // Xác định hoặc tạo khách hàng
            KhachHang khachHang;
            if (request.getKhachHangId() != null) {
                khachHang = khachHangService.getCustomerById(request.getKhachHangId());
            } else {
                // Kiểm tra dữ liệu khách hàng đầu vào
                if (request.getTenKhachHang() == null || request.getSdt() == null || request.getDiaChi() == null) {
                    return ResponseEntity.badRequest().body(Map.of("message", "Thông tin khách hàng không được để trống"));
                }

                // Kiểm tra xem khách hàng đã tồn tại chưa, nếu có thì sử dụng
                Optional<KhachHang> existingCustomer = khachHangService.findCustomerByPhone(request.getSdt());
                if (existingCustomer.isPresent()) {
                    khachHang = existingCustomer.get();
                } else {
                    khachHang = khachHangService.createCustomer(
                            request.getTenKhachHang(),
                            request.getSdt(),
                            request.getDiaChi()
                    );
                }
            }

            // Kiểm tra số lượng sản phẩm
            if (request.getSoLuong() == null || request.getSoLuong() <= 0) {
                return ResponseEntity.badRequest().body(Map.of("message", "Số lượng sản phẩm phải lớn hơn 0"));
            }

            // Tạo đơn hàng
            DonHang donHang = donHangService.createOrder(khachHang, sanPhamChiTiet, request.getSoLuong());

            // Trả về thông báo thành công
            return ResponseEntity.ok(Map.of(
                    "message", "Đặt hàng thành công!",
                    "orderId", donHang.getId()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            // Ghi log lỗi bất ngờ
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Có lỗi xảy ra trong quá trình xử lý"));
        }
    }

}

