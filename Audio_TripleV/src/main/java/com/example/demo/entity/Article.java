package com.example.demo.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "articles")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;   // Mã bài viết

    private String title;  // Tiêu đề bài viết
    private String imageUrl;
    @Column(name = "content", columnDefinition = "NVARCHAR(MAX)")
    private String content;
    // Nội dung bài viết
    private Date createdAt;  // Ngày giờ tạo bài viết
    private Date updatedAt;  // Ngày giờ cập nhật bài viết

    @OneToMany(mappedBy = "article") // Quan hệ 1-N với bảng comments
    private List<Comment> comments;

}
