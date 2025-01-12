package com.example.demo.repository;

import com.example.demo.entity.Article;
import com.example.demo.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findByArticleId(Integer articleId);

    List<Comment> findByArticle(Article article);

    @Modifying
    @Query("DELETE FROM Comment c WHERE c.id = :id")
    void deleteCommentById(@Param("id") Integer id);

    @Query("SELECT c FROM Comment c WHERE c.id = :id")
    Optional<Comment> findCommentById(@Param("id") Integer id);
}
