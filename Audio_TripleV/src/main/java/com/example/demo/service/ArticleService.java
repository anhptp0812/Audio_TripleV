package com.example.demo.service;

import com.example.demo.entity.Article;
import com.example.demo.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;
    // Lấy tất cả các bài viết
    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    public void saveArticle(Article article) {
        articleRepository.save(article);
    }

    public void deleteArticleById(Integer id) {
        articleRepository.deleteById(id);
    }

    public Article getArticleById(Integer id) {
        return articleRepository.findById(id).orElseThrow(() -> new RuntimeException("Article not found"));
    }

    public List<Article> findTop2NewestProducts() {
        return articleRepository.findTop2NewestProducts(PageRequest.of(0, 2));
    }

    public Article updateArticle(Article article) {
        // Kiểm tra tồn tại trước khi cập nhật
        if (articleRepository.existsById(article.getId())) {
            return articleRepository.save(article);
        } else {
            throw new RuntimeException("Không tìm thấy hãng với ID: " + article.getId());
        }
    }

    public Optional<Article> findById(Integer id) {
        return articleRepository.findById(id);
    }

    public void delete(Article article) {
        articleRepository.delete(article);
    }
}
