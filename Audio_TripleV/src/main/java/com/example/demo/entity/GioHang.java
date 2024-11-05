package com.example.demo.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "GioHang")
public class GioHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "ID_Khach_Hang")
    private KhachHang khachHang;

    @Column(name = "NgayThem")
    private Date ngayThem;

    @Column(name = "TrangThai")
    private String trangThai;

    // Quan hệ với GioHangChiTiet
    @OneToMany(mappedBy = "gioHang", cascade = CascadeType.ALL)
    private List<GioHangChiTiet> gioHangChiTietList;

    // Phương thức để thêm sản phẩm vào giỏ hàng
    public void themSanPham(GioHangChiTiet chiTiet) {
        gioHangChiTietList.add(chiTiet);
        chiTiet.setGioHang(this); // Thiết lập liên kết hai chiều
    }

    // Phương thức để tính tổng giá trị giỏ hàng
    public Double tinhTongGia() {
        return gioHangChiTietList.stream()
                .mapToDouble(GioHangChiTiet::tinhTongGia)
                .sum();
    }
}
