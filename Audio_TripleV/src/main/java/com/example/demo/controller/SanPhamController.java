package com.example.demo.controller;

import com.example.demo.entity.GioHang;
import com.example.demo.entity.KhachHang;
import com.example.demo.entity.SanPhamChiTiet;
import com.example.demo.service.SanPhamChiTietService;
import com.example.demo.service.KhachHangService;
import com.example.demo.service.GioHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/khach-hang")
public class SanPhamController {

    @Autowired
    private SanPhamChiTietService sanPhamChiTietService;

    @Autowired
    private KhachHangService khachHangService;

    @Autowired
    private GioHangService gioHangService;

    @GetMapping("/san-pham/hien-thi")
    public String getSanPhamChiTiet(@AuthenticationPrincipal UserDetails userDetails,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "12") int size,
                                    Model model) {
        // Kiểm tra nếu người dùng đã đăng nhập
        if (userDetails != null) {
            // Lấy thông tin khách hàng
            KhachHang khachHang = khachHangService.findByTaiKhoan(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("Khách hàng không tồn tại"));
            model.addAttribute("fullName", khachHang.getTen());

            // Lấy giỏ hàng
            GioHang gioHang = gioHangService.findByKhachHang(khachHang)
                    .orElseGet(() -> gioHangService.createGioHang(khachHang));

            // Tính tổng số lượng trong giỏ hàng
            int totalQuantity = gioHang.getGioHangChiTietList().stream()
                    .mapToInt(item -> item.getSoLuong())
                    .sum();
            model.addAttribute("cartCount", totalQuantity);
        } else {
            // Nếu người dùng chưa đăng nhập, gán giá trị mặc định
            model.addAttribute("fullName", "Khách");
            model.addAttribute("cartCount", 0);  // Giỏ hàng không có sản phẩm
        }

        // Lấy danh sách sản phẩm với phân trang
        Page<SanPhamChiTiet> pageResult = sanPhamChiTietService.getSanPhamChiTietWithPagination(page, size);

        // Định dạng giá sản phẩm thành VND
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        pageResult.getContent().forEach(sanPhamChiTiet -> {
            String formattedPrice = currencyFormat.format(sanPhamChiTiet.getDonGia());
            sanPhamChiTiet.setFormattedDonGia(formattedPrice);
        });

        model.addAttribute("sanPhamChiTietList", pageResult.getContent());
        model.addAttribute("totalPages", pageResult.getTotalPages());
        model.addAttribute("currentPage", page);
        return "customer/san-pham";
    }


    @GetMapping("/san-pham/loai/{idLoaiSP}")
    public String getSanPhamByLoai(
            @PathVariable int idLoaiSP,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model,
            @AuthenticationPrincipal UserDetails userDetails  // Lấy thông tin người dùng đã đăng nhập
    ) {
        // Lấy dữ liệu phân trang từ service
        Page<SanPhamChiTiet> sanPhamPage = sanPhamChiTietService.findByLoaiSanPham(idLoaiSP, page, size);

        // Đưa dữ liệu vào model
        model.addAttribute("sanPhamChiTietList", sanPhamPage.getContent());
        model.addAttribute("currentPage", sanPhamPage.getNumber());
        model.addAttribute("totalPages", sanPhamPage.getTotalPages());
        model.addAttribute("idLoaiSP", idLoaiSP);

        // Kiểm tra nếu userDetails có giá trị (người dùng đã đăng nhập)
        String displayName = (userDetails != null) ? userDetails.getUsername() : "Khách";
        model.addAttribute("fullName", displayName);

        return "customer/san-pham"; // Trả về tên view
    }





    @GetMapping("nhan-vien/hien-thi")
    public String getSanPhamChiTiet1(Model model) {
        List<SanPhamChiTiet> sanPhamChiTietList = sanPhamChiTietService.getAllSanPhamChiTiet();
        model.addAttribute("sanPhamChiTietList", sanPhamChiTietList);
        return "nhanvien/productProvity";
    }

    @GetMapping("/nhan-vien/loai/{idLoaiSP}")
    public String getSanPhamByLoaiWithPagination(
            @PathVariable int idLoaiSP,
            @RequestParam(defaultValue = "0") int page, // Trang mặc định là 0
            @RequestParam(defaultValue = "10") int size, // Kích thước mặc định là 10
            Model model
    ) {
        // Gọi service để lấy dữ liệu phân trang
        Page<SanPhamChiTiet> sanPhamPage = sanPhamChiTietService.findByLoaiSanPham(idLoaiSP, page, size);

        // Đưa dữ liệu vào model để hiển thị trong view
        model.addAttribute("sanPhamChiTietList", sanPhamPage.getContent()); // Danh sách sản phẩm
        model.addAttribute("currentPage", sanPhamPage.getNumber()); // Trang hiện tại
        model.addAttribute("totalPages", sanPhamPage.getTotalPages()); // Tổng số trang
        model.addAttribute("idLoaiSP", idLoaiSP); // Để giữ lại thông tin loại sản phẩm

        return "nhanvien/productProvity"; // Trả về tên view
    }

}

