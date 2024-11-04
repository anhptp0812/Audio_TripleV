package com.example.demo.controller;

import com.example.demo.entity.KhachHang;
import com.example.demo.repository.KhachHangRepo;
import com.example.demo.service.KhachHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
public class KhachHangController {
    @Autowired
    KhachHangRepo khachHangRepo;

    @GetMapping("/admin/khach-hang")
    public String getAllKhachHang(Model model) {
        List<KhachHang> list = khachHangRepo.findAll();
        model.addAttribute("data", list);
        return "admin/khach_hang"; // Trả về template HTML
    }

    @Autowired
    private KhachHangService khachHangService;

    @GetMapping("hien-thi")
    public List<KhachHang> hienThiKhachHang() {

        return khachHangRepo.findAll();
    }

    @GetMapping("/api/khach-hang/search")
    @ResponseBody
    public List<KhachHang> searchKhachHang(@RequestParam(required = false) String name,
                                           @RequestParam(required = false) String phone) {
        // Nếu cả hai đều null, trả về danh sách rỗng
        if (name == null && phone == null) {
            return Collections.emptyList();
        }
        return khachHangService.searchByNameAndPhone(name, phone); // Tìm kiếm khách hàng theo tên hoặc số điện thoại
    }


}
