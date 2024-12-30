package com.example.demo.controller;

import com.example.demo.entity.Article;
import com.example.demo.entity.Comment;
import com.example.demo.entity.GioHang;
import com.example.demo.entity.KhachHang;
import com.example.demo.service.ArticleService;
import com.example.demo.service.CommentService;
import com.example.demo.service.GioHangService;
import com.example.demo.service.KhachHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/khach-hang")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CommentService commentService;

    @Autowired
    KhachHangService khachHangService;

    @Autowired
    GioHangService gioHangService;

    // Hiển thị tất cả các bài viết và bình luận tương ứng
    @GetMapping("/bai-viet/hien-thi")
    public String showArticles(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        // Mặc định là "Khách" nếu chưa đăng nhập


        // Nếu người dùng đã đăng nhập, lấy thông tin khách hàng
        if (userDetails != null) {
            KhachHang khachHang = khachHangService.findByTaiKhoan(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("Khách hàng không tồn tại"));
            // Lấy tên khách hàng
            model.addAttribute("fullName", khachHang.getTen());


            // Lấy giỏ hàng, nếu đã đăng nhập sẽ tìm theo khách hàng, nếu chưa thì tạo giỏ hàng trống
            GioHang gioHang = (khachHang != null) ? gioHangService.findByKhachHang(khachHang)
                    .orElseGet(() -> new GioHang()) : new GioHang(); // Tạo giỏ hàng trống nếu không có khách hàng

            // Tính tổng số lượng sản phẩm trong giỏ hàng
            int totalQuantity = gioHang.getGioHangChiTietList().stream()
                    .mapToInt(item -> item.getSoLuong())
                    .sum();
            model.addAttribute("cartCount", totalQuantity);

        }else {
            // Nếu không đăng nhập, gán giá trị mặc định
            model.addAttribute("fullName", "Khách");
            model.addAttribute("cartCount", 0);
        }
        // Lấy tất cả các bài viết
        List<Article> articles = articleService.getAllArticles();
        model.addAttribute("articles", articles);

        return "customer/bai-viet"; // Trả về view danh sách bài viết
    }

    @GetMapping("/bai-viet/{id}")
    public String showArticleDetail(@AuthenticationPrincipal UserDetails userDetails,
                                    @PathVariable("id") Long articleId, Model model) {
        // Mặc định là "Khách" nếu chưa đăng nhập
        if (userDetails != null) {
            KhachHang khachHang = khachHangService.findByTaiKhoan(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("Khách hàng không tồn tại"));
            // Lấy tên khách hàng
            model.addAttribute("fullName", khachHang.getTen());


            // Lấy giỏ hàng, nếu đã đăng nhập sẽ tìm theo khách hàng, nếu chưa thì tạo giỏ hàng trống
            GioHang gioHang = (khachHang != null) ? gioHangService.findByKhachHang(khachHang)
                    .orElseGet(() -> new GioHang()) : new GioHang(); // Tạo giỏ hàng trống nếu không có khách hàng

            // Tính tổng số lượng sản phẩm trong giỏ hàng
            int totalQuantity = gioHang.getGioHangChiTietList().stream()
                    .mapToInt(item -> item.getSoLuong())
                    .sum();
            model.addAttribute("cartCount", totalQuantity);

        }else {
            // Nếu không đăng nhập, gán giá trị mặc định
            model.addAttribute("fullName", "Khách");
            model.addAttribute("cartCount", 0);
        }

        // Lấy chi tiết bài viết
        Article article = articleService.getArticleById(articleId);
        List<Comment> comments = commentService.getCommentsByArticleId(articleId);

        model.addAttribute("article", article);
        model.addAttribute("comments", comments);

        return "customer/bai-viet-detail"; // Trả về view chi tiết bài viết
    }


}
