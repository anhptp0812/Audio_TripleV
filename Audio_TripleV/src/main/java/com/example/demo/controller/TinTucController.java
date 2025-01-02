package com.example.demo.controller;

import com.example.demo.entity.Article;
import com.example.demo.entity.GioHang;
import com.example.demo.entity.KhachHang;
import com.example.demo.entity.SanPhamChiTiet;
import com.example.demo.service.ArticleService;
import com.example.demo.service.GioHangService;
import com.example.demo.service.KhachHangService;
import com.example.demo.service.SanPhamChiTietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Controller
@RequestMapping("/khach-hang")
public class TinTucController {
    @Autowired
    SanPhamChiTietService sanPhamChiTietService;

    @Autowired
    KhachHangService khachHangService;

    @Autowired
    GioHangService gioHangService;

    @Autowired
    private ArticleService articleService;


    @GetMapping("/trang-chu/hien-thi")
    public String showTop4SanPhamMoiAndSanPhamXemNhieuNhat(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        // Kiểm tra nếu người dùng đã đăng nhập
        if (userDetails != null) {
            KhachHang khachHang = khachHangService.findByTaiKhoan(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("Khách hàng không tồn tại"));
            model.addAttribute("fullName", khachHang.getTen());

            // Lấy giỏ hàng dựa trên khách hàng, nếu chưa có thì tạo mới
            GioHang gioHang = gioHangService.findByKhachHang(khachHang)
                    .orElseGet(() -> gioHangService.createGioHang(khachHang));

            // Tính tổng số lượng trong giỏ hàng
            int totalQuantity = 0;
            if (gioHang.getGioHangChiTietList() != null && !gioHang.getGioHangChiTietList().isEmpty()) {
                totalQuantity = gioHang.getGioHangChiTietList().stream()
                        .mapToInt(item -> item.getSoLuong())
                        .sum();
            }
            model.addAttribute("cartCount", totalQuantity);
        } else {
            // Nếu không đăng nhập, gán giá trị mặc định
            model.addAttribute("fullName", "Khách");
            model.addAttribute("cartCount", 0);
        }

        // Lấy các sản phẩm mới nhất
        List<SanPhamChiTiet> newestProducts = sanPhamChiTietService.getTop4NewestProducts();

        // Định dạng giá thành VND
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        for (SanPhamChiTiet product : newestProducts) {
            product.setFormattedDonGia(currencyFormat.format(product.getDonGia()));
        }

        model.addAttribute("sanPhamChiTietList", newestProducts);

        // Lấy các sản phẩm được xem nhiều nhất
        List<SanPhamChiTiet> sanPhamChiTietList = sanPhamChiTietService.getTop4SanPhamChiTiet();
        for (SanPhamChiTiet product : sanPhamChiTietList) {
            product.setFormattedDonGia(currencyFormat.format(product.getDonGia()));
        }
        List<Article> articles = articleService.findTop2NewestProducts();
        model.addAttribute("articles", articles);
        model.addAttribute("sanPhamChiTietList1", sanPhamChiTietList);

        return "customer/tin-tuc";
    }

    @GetMapping("/san-pham/hien-thi/{id}")
    public String hienThiSanPham(@AuthenticationPrincipal UserDetails userDetails,
                                 Model model, @PathVariable("id") Integer id) {
        // Kiểm tra xem người dùng đã đăng nhập hay chưa
        if (userDetails != null) {
            // Người dùng đã đăng nhập, lấy thông tin khách hàng
            KhachHang khachHang = khachHangService.findByTaiKhoan(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("Khách hàng không tồn tại"));
            model.addAttribute("fullName", khachHang.getTen());

            // Lấy giỏ hàng dựa trên khách hàng, nếu chưa có thì tạo mới
            GioHang gioHang = gioHangService.findByKhachHang(khachHang)
                    .orElseGet(() -> gioHangService.createGioHang(khachHang));

            // Tính tổng số lượng trong giỏ hàng
            int totalQuantity = 0;
            if (gioHang.getGioHangChiTietList() != null && !gioHang.getGioHangChiTietList().isEmpty()) {
                totalQuantity = gioHang.getGioHangChiTietList().stream()
                        .mapToInt(item -> item.getSoLuong())
                        .sum();
            }
            model.addAttribute("cartCount", totalQuantity);
        } else {
            // Nếu người dùng chưa đăng nhập, gán giá trị mặc định
            model.addAttribute("fullName", "Khách");
            model.addAttribute("cartCount", 0);  // Giỏ hàng không có sản phẩm
        }

        // Lấy sản phẩm chi tiết theo id
        SanPhamChiTiet spct = sanPhamChiTietService.findById(id);
        if (spct == null) {
            throw new RuntimeException("Sản phẩm không tồn tại");
        }

        model.addAttribute("spct", spct);

        // Định dạng giá thành trước khi đưa vào model
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        model.addAttribute("formattedDonGia", currencyFormat.format(spct.getDonGia()));

        // Lấy danh sách sản phẩm cùng hãng
        List<SanPhamChiTiet> sanPhamTuongTu = sanPhamChiTietService.findByHangId(spct.getHang().getId());
        model.addAttribute("sanPhamTuongTu", sanPhamTuongTu);

        return "customer/san-pham-chi-tiet-khach-hang";
    }

}
