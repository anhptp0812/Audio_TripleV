package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/thong-ke")
public class ThongKeController {

    @GetMapping
    public String thongKePage() {
        return "admin/thong-ke"; // Trả về file thong-ke.html trong thư mục templates
    }

//    @GetMapping("/data")
//    @ResponseBody
//    public ResponseEntity<Map<String, Object>> getThongKeData(@RequestParam String timeFrame) {
//        Map<String, Object> response = new HashMap<>();
//
//        switch (timeFrame) {
//            case "day":
//                response.put("labels", List.of("Hôm nay", "Hôm qua")); // Giả lập nhãn
//                response.put("data", List.of(1000000, 800000)); // Tổng doanh thu theo ngày
//                break;
//
//            case "month":
//                response.put("labels", List.of("Tháng 1", "Tháng 2", "Tháng 3")); // Giả lập nhãn
//                response.put("data", List.of(10000000, 12000000, 15000000)); // Tổng doanh thu theo tháng
//                break;
//
//            case "year":
//                response.put("labels", List.of("2023", "2024")); // Giả lập nhãn
//                response.put("data", List.of(150000000, 180000000)); // Tổng doanh thu theo năm
//                break;
//
//            default:
//                return ResponseEntity.badRequest().build();
//        }
//
//        return ResponseEntity.ok(response); // Trả về dữ liệu thống kê dưới dạng JSON
//    }
}
