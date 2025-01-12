package com.example.demo.controller;


import com.example.demo.entity.LoaiSanPham;
import com.example.demo.service.LoaiSanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;


@Controller
@RequestMapping("/admin/loai-san-pham")
public class LoaiSanPhamController {

    @Autowired
    private LoaiSanPhamService loaiSanPhamService;

    @GetMapping("/hien-thi")
    public String hienThi(Model model) {
        model.addAttribute("loaisp", loaiSanPhamService.findAll());
        return "admin/loai-san-pham";
    }

    @GetMapping("/form-add")
    public String add() {
        return "admin/loai-san-pham-add";
    }

    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<String> addLoaiSP(@RequestBody LoaiSanPham loaiSanPham) {
        try {
            loaiSanPham.setNgayTao(new Date()); // Gán ngày tạo
            loaiSanPham.setNgayCapNhat(new Date()); // Gán ngày cập nhật
            loaiSanPhamService.save(loaiSanPham);
            return ResponseEntity.ok("Thêm loại sản phẩm thành công!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Có lỗi xảy ra khi thêm loại sản phẩm.");
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteLoaiSanPham(@PathVariable Integer id) {
        loaiSanPhamService.deleteById(id);
        return "redirect:/admin/loai-san-pham/hien-thi";
    }

    @GetMapping("/form-update/{id}")
    public String showUpdateForm(@PathVariable Integer id, Model model) {
        LoaiSanPham loaiSanPham = loaiSanPhamService.findById(id);
        model.addAttribute("loaiSanPham", loaiSanPham);
        return "admin/loai-san-pham-update";
    }

    @PutMapping("/update")
    @ResponseBody
    public ResponseEntity<String> updateLoaiSanPham(@RequestBody LoaiSanPham loaiSanPham) {
        try {
            // Cập nhật ngày cho màu sắc
            loaiSanPham.setNgayCapNhat(new Date());

            // Gọi service để cập nhật màu sắc
            LoaiSanPham updatedLoaiSanPham = loaiSanPhamService.updateLoaiSanPham(loaiSanPham);

            // Trả về phản hồi thành công
            return ResponseEntity.ok("Cập nhật Loại thành công!");
        } catch (Exception e) {
            // Trả về phản hồi lỗi nếu có ngoại lệ xảy ra
            return ResponseEntity.status(500).body("Có lỗi xảy ra khi cập nhật Loại: " + e.getMessage());
        }
    }
}
