package com.example.demo.controller;

import com.example.demo.entity.Hang;
import com.example.demo.service.HangService;
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
@RequestMapping("/admin/hang")
public class HangController {

    @Autowired
    private HangService hangService;

    @GetMapping("/hien-thi")
    public String hienThi(Model model) {
        model.addAttribute("hang", hangService.findAll());
        return "admin/hang";
    }

    // Hiển thị form thêm màu sắc
    @GetMapping("/form-add")
    public String add() {
        return "admin/hang-add";
    }


    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<String> addColor(@RequestBody Hang hang) {
        try {
            hang.setNgayTao(new Date()); // Gán ngày tạo
            hang.setNgayCapNhat(new Date()); // Gán ngày cập nhật
            hangService.save(hang);
            return ResponseEntity.ok("Thêm hãng thành công!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Có lỗi xảy ra khi thêm hãng.");
        }
    }

    // Xóa màu sắc
    @GetMapping("/delete/{id}")
    public String deleteColor(@PathVariable Integer id) {
        hangService.deleteById(id);
        return "redirect:/hang/hien-thi"; // Thêm tham số activated
    }

    @GetMapping("/form-update/{id}")
    public String showUpdateForm(@PathVariable Integer id, Model model) {
        Hang hang = hangService.findById(id);
        model.addAttribute("hang", hang);
        return "admin/hang-update";
    }

    @PutMapping("/update")
    @ResponseBody
    public ResponseEntity<String> updateHang(@RequestBody Hang hang) {
        try {
            // Cập nhật ngày cho
            hang.setNgayCapNhat(new Date());

            // Gọi service để cập nhật
            Hang updatedHang = hangService.updateHang(hang);

            // Trả về phản hồi thành công
            return ResponseEntity.ok("Cập nhật hãng thành công!");
        } catch (Exception e) {
            // Trả về phản hồi lỗi nếu có ngoại lệ xảy ra
            return ResponseEntity.status(500).body("Có lỗi xảy ra khi cập nhật hãng: " + e.getMessage());
        }
    }

}

