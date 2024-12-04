package com.example.demo.api;

import com.example.demo.entity.*;
import com.example.demo.repository.HangRepository;
import com.example.demo.repository.KhachHangRepository;
import com.example.demo.repository.LoaiSanPhamRepository;
import com.example.demo.repository.MauSacRepository;
import com.example.demo.repository.SanPhamChiTietRepository;
import com.example.demo.repository.SanPhamRepository;
import com.example.demo.service.KhachHangService;
import com.example.demo.service.NhanVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
//@RestController
public class NhanVienController {
    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;

    @Autowired
    private  KhachHangService khachHangService;

    @Autowired
    private KhachHangRepository khachHangRepository;

    @Autowired
    private NhanVienService nhanVienService;


    @Autowired
    private LoaiSanPhamRepository loaiSanPhamRepository;

    @Autowired
    private SanPhamRepository sanPhamRepository;

    @Autowired
    private MauSacRepository mauSacRepository;

    @Autowired
    private HangRepository hangRepository;

//    @Autowired
//    private SanPhamChiTietService

    @GetMapping("user/trang-chu")
    public String index() {
        return "nhanvien/tin_tuc";
    }

    @GetMapping("/user/ban-hang")
    public String banHang(@RequestParam(required = false) Integer idLoaiSP,
                          @RequestParam(required = false) Integer idSanPham,
                          @RequestParam(required = false) Integer mauSac,
                          @RequestParam(required = false) Integer hang,
                          @RequestParam(required = false) Double minPrice,
                          @RequestParam(required = false) Double maxPrice,
                          @RequestParam(required = false) String donGia, // Thêm tham số donGia
                          @RequestParam(required = false) String tenSanPham, // Thêm tham số tenSanPham
                          Model model) {

        List<SanPhamChiTiet> list;

        // Xử lý khoảng giá nếu có
        if (donGia != null && !donGia.isEmpty()) {
            String[] priceRange = donGia.split("-");
            if (priceRange.length == 2) {
                minPrice = Double.parseDouble(priceRange[0]);
                maxPrice = Double.parseDouble(priceRange[1]);
            } else if (priceRange.length == 1 && priceRange[0].endsWith("+")) {
                minPrice = Double.parseDouble(priceRange[0].replace("+", ""));
                maxPrice = Double.MAX_VALUE;
            }
        }

        // Kiểm tra các bộ lọc và lấy sản phẩm tương ứng
        if ((idLoaiSP == null || idLoaiSP == 0) &&
                (idSanPham == null || idSanPham == 0) &&
                (mauSac == null || mauSac == 0) &&
                (hang == null || hang == 0) &&
                (minPrice == null || maxPrice == null) &&
                (tenSanPham == null || tenSanPham.isEmpty())) {
            // Nếu không chọn bộ lọc nào thì lấy tất cả sản phẩm
            list = sanPhamChiTietRepository.findAll();
        } else {
            // Lấy danh sách sản phẩm theo các bộ lọc
            list = sanPhamChiTietRepository.findByFilters(
                    idLoaiSP, idSanPham, mauSac, hang, minPrice, maxPrice, tenSanPham);
        }

        // Thêm danh sách sản phẩm vào model
        model.addAttribute("spct", list);

        // Lấy tất cả loại sản phẩm, màu sắc, hãng để hiển thị trong form
        List<SanPham> sanPhams = sanPhamRepository.findAll();
        model.addAttribute("sanPhams", sanPhams);
        List<MauSac> mauSacs = mauSacRepository.findAll();
        model.addAttribute("mauSacs", mauSacs);
        List<Hang> hangs = hangRepository.findAll();
        model.addAttribute("hangs", hangs);
        List<LoaiSanPham> loaiSanPhams = loaiSanPhamRepository.findAll();
        model.addAttribute("loaiSanPhams", loaiSanPhams);

        // Lấy danh sách khách hàng nếu cần
        List<KhachHang> list1 = khachHangRepository.findAll();
        model.addAttribute("listKh", list1);

        // Truyền đối tượng DonHang vào model để có thể sử dụng trong form
        model.addAttribute("donHang", new DonHang());
        model.addAttribute("donHangChiTiet", new DonHangChiTiet());

        return "nhanvien/productProvity"; // Trả về trang sản phẩm
    }


    @GetMapping("/user/thong-tin")
    public String thongTinTaiKhoanNV(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        // Lấy khách hàng hiện tại
        NhanVien nhanVien = nhanVienService.findByTaiKhoan(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Nhân Viên không tồn tại"));
        model.addAttribute("nhanVien", nhanVien);

//        // Lấy danh sách đơn hàng
//        List<DonHang> donHangList = donHangService.findByKhachHang(khachHang);
//
//        model.addAttribute("donHangList", donHangList);
//        model.addAttribute("counter", 0);
//
//        // Lấy giỏ hàng dựa trên khách hàng, nếu chưa có thì tạo mới
//        GioHang gioHang = gioHangService.findByKhachHang(khachHang)
//                .orElseGet(() -> gioHangService.createGioHang(khachHang));
//
//        // Tính tổng số lượng trong giỏ hàng
//        int totalQuantity = 0; // For cart count
//        if (gioHang.getGioHangChiTietList() != null && !gioHang.getGioHangChiTietList().isEmpty()) {
//            totalQuantity = gioHang.getGioHangChiTietList().stream()
//                    .mapToInt(item -> item.getSoLuong())
//                    .sum();
//        }
//        model.addAttribute("cartCount", totalQuantity);khoan

        return "nhanvien/tai-khoan-user"; // Trang hiển thị danh sách đơn hàng
    }

}
