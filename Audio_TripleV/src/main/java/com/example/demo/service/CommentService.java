package com.example.demo.service;

import com.example.demo.entity.Article;
import com.example.demo.entity.Comment;
import com.example.demo.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    // Lấy các bình luận của bài viết
    public List<Comment> getCommentsByArticleId(Integer articleId) {
        return commentRepository.findByArticleId(articleId);
    }

    // Lưu bình luận
    public void saveComment(Comment comment) {
        commentRepository.save(comment);
    }

    public List<Comment> getCommentsByArticle(Article article) {
        return commentRepository.findByArticle(article);
    }

    public void deleteCommentById(Integer id) {
        commentRepository.deleteById(id);
    }

}
