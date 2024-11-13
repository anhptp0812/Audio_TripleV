package com.example.demo.controller;

import com.example.demo.entity.MauSac;
import com.example.demo.service.MauSacService;
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
@RequestMapping("/mau-sac")
public class MauSacController {

    @Autowired
    private MauSacService mauSacService;

    // Hiển thị form thêm màu sắc
    @GetMapping("/form-add")
    public String add() {
        return "admin/mau-sac-add";
    }

    // Xử lý thêm màu sắc bằng JSON cho AJAX
    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<String> addColor(@RequestBody MauSac mauSac) {
        try {
            mauSac.setNgayTao(new Date()); // Gán ngày tạo
            mauSac.setNgayCapNhat(new Date()); // Gán ngày cập nhật
            mauSacService.save(mauSac);
            return ResponseEntity.ok("Thêm màu sắc thành công!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Có lỗi xảy ra khi thêm màu sắc.");
        }
    }

    @GetMapping("/form-update/{id}")
    public String showUpdateForm(@PathVariable Integer id, Model model) {
        MauSac mauSac = mauSacService.findById(id);
        model.addAttribute("mauSac", mauSac);
        return "admin/mau-sac-update";
    }

    @PutMapping("/update")
    @ResponseBody
    public ResponseEntity<String> updateMauSac(@RequestBody MauSac mauSac) {
        try {
            // Cập nhật ngày cho màu sắc
            mauSac.setNgayCapNhat(new Date());

            // Gọi service để cập nhật màu sắc
            MauSac updatedMauSac = mauSacService.updateMauSac(mauSac);

            // Trả về phản hồi thành công
            return ResponseEntity.ok("Cập nhật màu sắc thành công!");
        } catch (Exception e) {
            // Trả về phản hồi lỗi nếu có ngoại lệ xảy ra
            return ResponseEntity.status(500).body("Có lỗi xảy ra khi cập nhật màu sắc: " + e.getMessage());
        }
    }


    // Xóa màu sắc
    @GetMapping("/delete/{id}")
    public String deleteColor(@PathVariable Integer id) {
        mauSacService.deleteById(id);
        return "redirect:/spct/hien-thi?activated=colors"; // Thêm tham số activated
    }
}
