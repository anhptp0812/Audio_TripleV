package com.example.demo.repository;

import com.example.demo.entity.Article;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Integer> {
    @Query("SELECT a FROM Article a ORDER BY a.createdAt DESC")
    List<Article> findTop2NewestProducts(Pageable pageable);
}
