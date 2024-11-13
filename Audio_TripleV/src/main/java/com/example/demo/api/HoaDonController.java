package com.example.demo.api;

import com.example.demo.entity.DonHang;
import com.example.demo.entity.DonHangChiTiet;
import com.example.demo.entity.Hang;
import com.example.demo.entity.HoaDon;
import com.example.demo.entity.HoaDonChiTiet;
import com.example.demo.entity.LoaiSanPham;
import com.example.demo.entity.MauSac;
import com.example.demo.entity.SanPham;
import com.example.demo.entity.SanPhamChiTiet;
import com.example.demo.entityCustom.DonHangRepository;
import com.example.demo.repository.DonHangChiTietRepository;
import com.example.demo.repository.HangRepository;
import com.example.demo.repository.HoaDonChiTietRepository;
import com.example.demo.repository.HoaDonRepository;
import com.example.demo.repository.LoaiSanPhamRepository;
import com.example.demo.repository.MauSacRepository;
import com.example.demo.repository.SanPhamChiTietRepository;
import com.example.demo.repository.SanPhamRepository;
import com.example.demo.service.DonHangService;
import com.example.demo.service.HoaDonService;
import com.example.demo.service.SanPhamChiTietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class HoaDonController {
    @Autowired
    private DonHangService donHangService;

    @Autowired
    private DonHangRepository donHangRepository;

    @Autowired
    private DonHangChiTietRepository donHangChiTietRepository;

    @Autowired
    SanPhamChiTietRepository sanPhamChiTietRepository;

    @Autowired
    private SanPhamChiTietService sanPhamChiTietService;

    @Autowired
    private LoaiSanPhamRepository loaiSanPhamRepository;

    @Autowired
    private SanPhamRepository sanPhamRepository;

    @Autowired
    private MauSacRepository mauSacRepository;

    @Autowired
    private HangRepository hangRepository;

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Autowired
    private HoaDonChiTietRepository hoaDonChiTietRepository;

    @Autowired
    private HoaDonService hoaDonService;




    @PostMapping("/ban-hang/save")
        public ResponseEntity<Map<String, String>> saveDonHang(@ModelAttribute("donHang") HoaDon hoaDon,
                                                               HoaDonChiTiet hoaDonChiTiet) {

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

            // Lưu hóa đơn
            hoaDonRepository.save(hoaDon);

            // Trả về URL của trang đích sau khi lưu thành công
            Map<String, String> response = new HashMap<>();
            response.put("redirectUrl", "/user/ban-hang/" + hoaDon.getId());

            return ResponseEntity.ok(response);
        }


}

