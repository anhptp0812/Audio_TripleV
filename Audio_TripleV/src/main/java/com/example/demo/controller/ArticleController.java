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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
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
        // Lấy thông tin khách hàng
        KhachHang khachHang = khachHangService.findByTaiKhoan(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Khách hàng không tồn tại"));
        model.addAttribute("fullName", khachHang.getTen());

        // Lấy giỏ hàng
        GioHang gioHang = gioHangService.findByKhachHang(khachHang)
                .orElseGet(() -> gioHangService.createGioHang(khachHang));
        int totalQuantity = gioHang.getGioHangChiTietList().stream()
                .mapToInt(item -> item.getSoLuong())
                .sum();

        model.addAttribute("cartCount", totalQuantity);
        List<Article> articles = articleService.getAllArticles();
        model.addAttribute("articles", articles);
        return "customer/bai-viet";
    }

    // Hiển thị chi tiết bài viết cùng bình luận của nó
    @GetMapping("/bai-viet/{id}")
    public String showArticleDetail(@AuthenticationPrincipal UserDetails userDetails,
                                    @PathVariable("id") Integer articleId, Model model) {
        // Lấy thông tin khách hàng
        KhachHang khachHang = khachHangService.findByTaiKhoan(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Khách hàng không tồn tại"));
        model.addAttribute("fullName", khachHang.getTen());

        // Lấy giỏ hàng
        GioHang gioHang = gioHangService.findByKhachHang(khachHang)
                .orElseGet(() -> gioHangService.createGioHang(khachHang));
        int totalQuantity = gioHang.getGioHangChiTietList().stream()
                .mapToInt(item -> item.getSoLuong())
                .sum();
        Article article = articleService.getArticleById(articleId);
        List<Comment> comments = commentService.getCommentsByArticleId(articleId);
        model.addAttribute("cartCount", totalQuantity);
        model.addAttribute("article", article);
        model.addAttribute("comments", comments);
        return "customer/bai-viet-detail";
    }
}
