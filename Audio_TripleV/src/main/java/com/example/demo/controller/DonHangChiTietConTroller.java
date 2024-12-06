package com.example.demo.controller;

import com.example.demo.entity.DonHang;
import com.example.demo.entity.DonHangChiTiet;
import com.example.demo.entity.HoaDonChiTiet;
import com.example.demo.entity.SanPhamChiTiet;
import com.example.demo.repository.DonHangChiTietRepository;
import com.example.demo.repository.HoaDonChiTietRepository;
import com.example.demo.repository.HoaDonRepository;
import com.example.demo.repository.SanPhamChiTietRepository;
import com.example.demo.service.DonHangService;
//import com.example.demo.vnPay.VNPayConfig;
import com.example.demo.service.HoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
public class DonHangChiTietConTroller {


    @Autowired
    private DonHangService donHangService;

    @Autowired
    private DonHangChiTietRepository donHangChiTietRepository;

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Autowired
    private HoaDonChiTietRepository hoaDonChiTietRepository;

    @Autowired
    private HoaDonService hoaDonService;

    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;

    @GetMapping("ban-hang/detail/{id}")
    private List<DonHangChiTiet> index(Integer id) {
        return donHangService.findByDHid(id);
    }


    @GetMapping("ban-hang/{id}/details")
    @ResponseBody
    public List<Map<String, Object>> getOrderDetails(@PathVariable Integer id) {
        List<HoaDonChiTiet> chiTietList = hoaDonService.findByDHid(id);
        return chiTietList.stream().map(chiTiet -> {
            Map<String, Object> map = new HashMap<>();
            map.put("productName", chiTiet.getSanPhamChiTiet().getSanPham().getTen());
            map.put("quantity", chiTiet.getSoLuong());
            map.put("price", chiTiet.getDonGia());
            map.put("total", chiTiet.getSoLuong() * chiTiet.getDonGia());
            return map;
        }).collect(Collectors.toList());
    }

    @DeleteMapping("/ban-hang/delete/{spctId}")
    public ResponseEntity<?>deleteOrderDetail(@PathVariable Integer spctId) {
        Optional<HoaDonChiTiet> hoaDonChiTietOptional = hoaDonChiTietRepository.findById(spctId);

        if (hoaDonChiTietOptional.isPresent()) {
            HoaDonChiTiet hoaDonChiTiet = hoaDonChiTietOptional.get();
            SanPhamChiTiet sanPhamChiTiet = hoaDonChiTiet.getSanPhamChiTiet();
            sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() + hoaDonChiTiet.getSoLuong());
            sanPhamChiTietRepository.save(sanPhamChiTiet);
            hoaDonChiTietRepository.delete(hoaDonChiTiet);

            return ResponseEntity.ok("Order detail deleted successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order detail not found");
    }


//    @DeleteMapping("/ban-hang/delete/{dhctId}")
//    @ResponseBody
//    public ResponseEntity<Map<String, Object>> deleteOrderDetail(@PathVariable Integer dhctId) {
//        Map<String, Object> response = new HashMap<>();
//
//        // Kiểm tra giá trị dhctId có hợp lệ không
//        if (dhctId == null) {
//            response.put("success", false);
//            response.put("message", "ID chi tiết đơn hàng không hợp lệ.");
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
//        }
//
//        // Tìm chi tiết đơn hàng theo dhctId
//        Optional<DonHangChiTiet> optionalDonHangChiTiet = donHangChiTietRepository.findById(dhctId);
//        if (optionalDonHangChiTiet.isEmpty()) {
//            response.put("success", false);
//            response.put("message", "Chi tiết đơn hàng không tồn tại.");
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
//        }
//
//        DonHangChiTiet donHangChiTiet = optionalDonHangChiTiet.get();
//        SanPhamChiTiet sanPhamChiTiet = donHangChiTiet.getSanPhamChiTiet();
//
//        // Tăng số lượng lại trong SanPhamChiTiet
//        sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() + donHangChiTiet.getSoLuong());
//        sanPhamChiTietRepository.save(sanPhamChiTiet);
//
//        // Xóa chi tiết đơn hàng
//        donHangChiTietRepository.delete(donHangChiTiet);
//
//        response.put("success", true);
//        response.put("message", "Chi tiết đơn hàng đã được xóa.");
//        return ResponseEntity.ok(response);
//    }




}
