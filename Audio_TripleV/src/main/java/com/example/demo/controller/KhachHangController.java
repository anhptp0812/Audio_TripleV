package com.example.demo.controller;

import com.example.demo.entity.DonHang;
import com.example.demo.entity.DonHangChiTiet;
import com.example.demo.entity.GioHang;
import com.example.demo.entity.KhachHang;
import com.example.demo.entity.SanPhamChiTiet;
import com.example.demo.service.DonHangService;
import com.example.demo.service.GioHangService;
import com.example.demo.service.KhachHangService;
import com.example.demo.repository.KhachHangRepository;
import com.example.demo.service.SanPhamChiTietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class KhachHangController {
    @Autowired
    private KhachHangRepository khachHangRepository;

    @Autowired
    private KhachHangService khachHangService;

    @GetMapping("hien-thi")
    public List<KhachHang> hienThiKhachHang() {

        return khachHangRepository.findAll();
    }

    @GetMapping("/api/khach-hang/search")
    @ResponseBody
    public List<KhachHang> searchKhachHang(@RequestParam(required = false) String name,
                                           @RequestParam(required = false) String phone) {
        // Nếu cả hai đều null, trả về danh sách rỗng
        if (name == null && phone == null) {
            return Collections.emptyList();
        }
        return khachHangService.searchByNameAndPhone(name, phone); // Tìm kiếm khách hàng theo tên hoặc số điện thoại
    }

    @PostMapping("/api/khach-hang/save")
    public ResponseEntity<KhachHang> saveKh(@RequestBody KhachHang khachHang) {
        KhachHang savedKhachHang = khachHangRepository.save(khachHang);
        return ResponseEntity.ok(savedKhachHang);
    }

    @Autowired
    private GioHangService gioHangService;

    @Autowired
    private DonHangService donHangService;

    @Autowired
    private SanPhamChiTietService sanPhamChiTietService;

    @GetMapping("/khach-hang/don-hang/chot-don")
    public String chotDon(@AuthenticationPrincipal UserDetails userDetails) {
        // Lấy khách hàng hiện tại
        KhachHang khachHang = khachHangService.findByTaiKhoan(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Khách hàng không tồn tại"));

        // Lấy giỏ hàng của khách hàng
        GioHang gioHang = gioHangService.findByKhachHang(khachHang)
                .orElseThrow(() -> new RuntimeException("Giỏ hàng không tồn tại"));

        // Tạo đơn hàng mới
        DonHang donHang = new DonHang();
        donHang.setKhachHang(khachHang);
        donHang.setTongGia(gioHang.getTongGia());
        donHang.setTrangThai("Chờ xử lý");
        donHang.setNgayTao(new Date());

        // Chuyển các sản phẩm từ giỏ hàng sang chi tiết đơn hàng và cập nhật số lượng
        List<DonHangChiTiet> chiTietList = gioHang.getGioHangChiTietList().stream()
                .map(item -> {
                    // Giảm số lượng sản phẩm chi tiết
                    SanPhamChiTiet sanPhamChiTiet = item.getSanPhamChiTiet();
                    int soLuongTrongKho = sanPhamChiTiet.getSoLuong();
                    int soLuongDatMua = item.getSoLuong();

                    if (soLuongDatMua > soLuongTrongKho) {
                        throw new RuntimeException("Sản phẩm " + sanPhamChiTiet.getSanPham().getTen() + " không đủ số lượng trong kho.");
                    }

                    sanPhamChiTiet.setSoLuong(soLuongTrongKho - soLuongDatMua);
                    sanPhamChiTietService.addSanPhamChiTiet(sanPhamChiTiet); // Cập nhật số lượng sản phẩm chi tiết trong DB

                    // Tạo chi tiết đơn hàng
                    DonHangChiTiet chiTiet = new DonHangChiTiet();
                    chiTiet.setSanPhamChiTiet(sanPhamChiTiet);
                    chiTiet.setSoLuong(soLuongDatMua);
                    chiTiet.setDonGia(sanPhamChiTiet.getDonGia());
                    chiTiet.setDonHang(donHang);
                    chiTiet.setNgayTao(new Date());
                    return chiTiet;
                })
                .collect(Collectors.toList());

        donHang.setDonHangChiTietList(chiTietList);

        // Lưu đơn hàng vào cơ sở dữ liệu
        donHangService.save(donHang);

        // Xóa giỏ hàng sau khi tạo đơn hàng
        gioHangService.clearGioHang(gioHang);

        // Chuyển hướng sang trang "Đơn hàng của tôi"
        return "redirect:/khach-hang/don-hang/danh-sach";
    }

    @GetMapping("/khach-hang/don-hang/danh-sach")
    public String danhSachDonHang(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        // Lấy khách hàng hiện tại
        KhachHang khachHang = khachHangService.findByTaiKhoan(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Khách hàng không tồn tại"));
        model.addAttribute("fullName", khachHang.getTen());

        // Lấy danh sách đơn hàng
        List<DonHang> donHangList = donHangService.findByKhachHang(khachHang);

        model.addAttribute("donHangList", donHangList);
        model.addAttribute("counter", 0);

        // Lấy giỏ hàng dựa trên khách hàng, nếu chưa có thì tạo mới
        GioHang gioHang = gioHangService.findByKhachHang(khachHang)
                .orElseGet(() -> gioHangService.createGioHang(khachHang));

        // Tính tổng số lượng trong giỏ hàng
        int totalQuantity = 0; // For cart count
        if (gioHang.getGioHangChiTietList() != null && !gioHang.getGioHangChiTietList().isEmpty()) {
            totalQuantity = gioHang.getGioHangChiTietList().stream()
                    .mapToInt(item -> item.getSoLuong())
                    .sum();
        }
        model.addAttribute("cartCount", totalQuantity);

        return "customer/don-hang-cua-toi"; // Trang hiển thị danh sách đơn hàng
    }

    @GetMapping("/khach-hang/thong-tin")
    public String thongTinTaiKhoan(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        // Lấy khách hàng hiện tại
        KhachHang khachHang = khachHangService.findByTaiKhoan(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Khách hàng không tồn tại"));
        model.addAttribute("khachHang", khachHang);

        return "customer/tai-khoan";
    }

}
