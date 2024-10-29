package com.example.demo.entity;

import jakarta.persistence.*;
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
@Table(name = "Hang")
public class Hang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "Ten")
    private String ten;

    @Temporal(TemporalType.DATE)  // Chỉ định kiểu DATE
    @Column(name = "NgayTao")
    private Date ngayTao;

    @Temporal(TemporalType.DATE)  // Chỉ định kiểu DATE
    @Column(name = "NgayCapNhat")
    private Date ngayCapNhat;

    @Override
    public String toString() {
        return "Hang{" +
                "id=" + id +
                ", ten='" + ten + '\'' +
                ", ngayTao=" + ngayTao +
                ", ngayCapNhat=" + ngayCapNhat +
                '}';
    }
}
