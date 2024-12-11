package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.repository.DonHangChiTietRepository;
import com.example.demo.repository.DonHangRepository;
import com.example.demo.repository.HangRepository;
import com.example.demo.repository.HoaDonChiTietRepository;
import com.example.demo.repository.HoaDonRepository;
import com.example.demo.repository.LoaiSanPhamRepository;
import com.example.demo.repository.MauSacRepository;
import com.example.demo.repository.NhanVienRepo;
import com.example.demo.repository.SanPhamChiTietRepository;
import com.example.demo.repository.SanPhamRepository;
import com.example.demo.service.DonHangService;
import com.example.demo.service.HoaDonService;
import com.example.demo.service.KhachHangService;
import com.example.demo.service.NhanVienService;
import com.example.demo.service.SanPhamChiTietService;
//import com.example.demo.service.VnPayService;
//import com.example.demo.vnPay.VNPayConfig;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.*;

import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000") // Thay đổi URL này theo miền của frontend
@Controller
@RequestMapping("/user")
public class DonHangController {
    //    @Autowired
//    private VNPayConfig vnpayConfig;
    @Autowired
    private NhanVienService nhanVienService;

    @Autowired
    private DonHangService donHangService;

    @Autowired
    private DonHangRepository donHangRepository;

    @Autowired
    private DonHangChiTietRepository donHangChiTietRepository;

    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;

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

    @Autowired
    private KhachHangService khachHangService;

    @Autowired
    private NhanVienRepo nhanVienRepo;

    @GetMapping("/don-hang")
    public String index(@RequestParam(value = "trangThai", required = false) String trangThai, Model model) {
        List<DonHang> list;

        // Lọc đơn hàng theo trạng thái nếu có
        if (trangThai != null && !trangThai.isEmpty()) {
            list = donHangRepository.findByTrangThai(trangThai);
        } else {
            list = donHangRepository.findAll();
        }

        // Thông báo nếu không có đơn hàng phù hợp
        if (list.isEmpty()) {
            model.addAttribute("message", "Không có đơn hàng phù hợp với tiêu chí tìm kiếm.");
        }
        model.addAttribute("listDH", list);

        // Lựa chọn trạng thái có sẵn
        List<String> trangThaiOptions = List.of("Chờ xử lý", "Đã xác nhận", "Đang giao hàng", "Đã giao hàng", "Đã hủy");
        model.addAttribute("trangThaiOptions", trangThaiOptions);

        // Thêm đối tượng DonHang vào model
        model.addAttribute("dh", new DonHang()); // Thêm đối tượng DonHang rỗng vào model

        return "nhanvien/donhang"; // Trả về file HTML
    }

    @PostMapping("/don-hang/cap-nhat-trang-thai")
    public String capNhatTrangThai(@ModelAttribute("dh") DonHang dh,
                                   @RequestParam("id") Integer id,
                                   @RequestParam("trangThai") String trangThai,
                                   RedirectAttributes redirectAttributes) {
        // Tìm đơn hàng theo ID
        DonHang donHang = donHangRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng với ID: " + id));

        String currentTrangThai = donHang.getTrangThai();

        // Kiểm tra trạng thái hiện tại và trạng thái mới
        if (currentTrangThai.equals(trangThai)) {
            redirectAttributes.addFlashAttribute("message", "Trạng thái đơn hàng không thay đổi.");
            return "redirect:/user/don-hang";
        }

        // Cập nhật ngày khi thay đổi trạng thái
        if ("Chờ xử lý".equals(currentTrangThai) && "Đã hủy".equals(trangThai)) {
            donHang.setTrangThaiPayment("Hủy thanh toán");
            donHang.setNgayCapNhat(new Date());
        } else if ("Chờ xử lý".equals(currentTrangThai) && "Đã xác nhận".equals(trangThai)) {
            donHang.setNgayCapNhat(new Date());
        } else if ("Đã xác nhận".equals(currentTrangThai) && "Đang giao hàng".equals(trangThai)) {
            donHang.setNgayCapNhat(new Date());
        } else if ("Đang giao hàng".equals(currentTrangThai) && "Đã giao hàng".equals(trangThai)) {
            donHang.setTrangThaiPayment("Đã thanh toán");
            donHang.setNgayGiao(new Date());
        }

        // Cập nhật trạng thái
        donHang.setTrangThai(trangThai);
        donHangRepository.save(donHang);

        // Thêm thông báo thành công
        redirectAttributes.addFlashAttribute("message", "Cập nhật trạng thái đơn hàng thành công!");

        return "redirect:/user/don-hang";  // Quay lại trang danh sách đơn hàng
    }
}


