package com.example.demo.controller;

import com.example.demo.entity.DonHang;
import com.example.demo.entity.DonHangChiTiet;
import com.example.demo.service.DonHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("user")
public class DonHangChiTietConTroller {
    @Autowired
    private DonHangService donHangService;

    @GetMapping("ban-hang/detail/{id}")
    private List<DonHangChiTiet> index(Integer id) {
        return donHangService.findByDHid(id);
    }

    @GetMapping("ban-hang/{id}/details")
    @ResponseBody
    public List<Map<String, Object>> getOrderDetails(@PathVariable Integer id) {
        List<DonHangChiTiet> chiTietList = donHangService.findByDHid(id);
        return chiTietList.stream().map(chiTiet -> {
            Map<String, Object> map = new HashMap<>();
            map.put("productName", chiTiet.getSanPhamChiTiet().getSanPham().getTen());
            map.put("quantity", chiTiet.getSoLuong());
            map.put("price", chiTiet.getDonGia());
            map.put("total", chiTiet.getSoLuong() * chiTiet.getDonGia());
            return map;
        }).collect(Collectors.toList());
    }


}
