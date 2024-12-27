package com.example.demo.controller;

import com.example.demo.entity.Article;
import com.example.demo.entity.Comment;
import com.example.demo.entity.KhachHang;
import com.example.demo.service.CommentService;
import com.example.demo.service.ArticleService;
import com.example.demo.service.KhachHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/khach-hang")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private KhachHangService khachHangService;

    // Xử lý gửi bình luận
    @PostMapping("/bai-viet/{id}/comment")
    public String addComment(@PathVariable("id") Long articleId, String content) {
        Article article = articleService.getArticleById(articleId);
        KhachHang khachHang = khachHangService.getCurrentUser(); // Giả sử có hàm này để lấy người dùng hiện tại

        Comment comment = new Comment();
        comment.setArticle(article);
        comment.setKhachHang(khachHang);
        comment.setContent(content);
        commentService.saveComment(comment);

        return "redirect:/khach-hang/bai-viet/{id}";
    }
}
