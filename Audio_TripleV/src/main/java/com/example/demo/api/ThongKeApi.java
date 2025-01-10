package com.example.demo.api;

import com.example.demo.service.ThongKeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@RestController
    @RequestMapping("/api/thong-ke")
    public class ThongKeApi {
        @Autowired
        private ThongKeService thongKeService;

    @GetMapping("/ngay")
    public ResponseEntity<Double> thongKeTheoNgay(@RequestParam String date) {
        try {
            LocalDate localDate = LocalDate.parse(date); // Parse ngày từ String
            Double tongDoanhThu = thongKeService.tinhTongDoanhThuTheoNgay(localDate);
            return ResponseEntity.ok(tongDoanhThu != null ? tongDoanhThu : 0.0);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body(0.0); // Trả về lỗi nếu ngày không hợp lệ
        } catch (Exception e) {
            // Log chi tiết lỗi để debug
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(0.0);
        }
    }


    @GetMapping("/thang")
    public ResponseEntity<Double> thongKeTheoThang(@RequestParam int month, @RequestParam int year) {
        Double tongDoanhThu = thongKeService.tinhTongDoanhThuTheoThang(month, year);
        return ResponseEntity.ok(tongDoanhThu != null ? tongDoanhThu : 0.0);
    }

    @GetMapping("/nam")
    public ResponseEntity<Double> thongKeTheoNam(@RequestParam int year) {
        Double tongDoanhThu = thongKeService.tinhTongDoanhThuTheoNam(year);
        return ResponseEntity.ok(tongDoanhThu != null ? tongDoanhThu : 0.0);
    }
}
