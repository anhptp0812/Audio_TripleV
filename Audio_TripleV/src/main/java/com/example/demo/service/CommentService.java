package com.example.demo.service;

import com.example.demo.entity.Article;
import com.example.demo.entity.Comment;
import com.example.demo.repository.CommentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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


    @Transactional
    public boolean deleteComment(Integer id) {
        try {
            Optional<Comment> comment = commentRepository.findById(id);
            if (comment.isPresent()) {
                commentRepository.delete(comment.get());
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
