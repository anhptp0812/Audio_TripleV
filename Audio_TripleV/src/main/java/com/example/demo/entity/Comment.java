package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;  // Mã bình luận

    @ManyToOne
    @JoinColumn(name = "article_id", nullable = false)
    private Article article; // Bài viết liên kết với bình luận

    @ManyToOne
    @JoinColumn(name = "ID_Khach_Hang")
    private KhachHang khachHang; // Khách hàng (tác giả bình luận)

    @Column(name = "content", columnDefinition = "NVARCHAR(MAX)")
    private String content; // Nội dung bình luận

    @Column(name = "created_at")
    private Date createdAt;  // Ngày giờ tạo bình luận

}
