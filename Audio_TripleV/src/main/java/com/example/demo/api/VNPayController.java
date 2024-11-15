package com.example.demo.api;

import com.example.demo.entity.HoaDon;
import com.example.demo.repository.HoaDonRepository;
import com.example.demo.service.VNPayService;
import com.example.demo.vnPay.VNPayConfig;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class VNPayController {

    @Autowired
    private VNPayService vnPayService;

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @GetMapping("/create-payment")
    public String createPayment(HttpServletRequest request,
                                @RequestParam("amount") Long amount,
                                @RequestParam(value = "bankCode", required = false) String bankCode,
                                @RequestParam(value = "orderId", required = false) Integer orderId) {
        try {
            // Gọi Service để tạo URL thanh toán
            String paymentUrl = vnPayService.createVNPayUrl(request, amount, bankCode, orderId);
            return "redirect:" + paymentUrl; // Chuyển hướng người dùng đến VNPay
        } catch (Exception e) {
            return "Lỗi: " + e.getMessage();
        }
    }

    @GetMapping("/vnpay_return")
    public ResponseEntity<?> handleVNPayReturn(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        request.getParameterMap().forEach((key, value) -> params.put(key, value[0]));

        String secureHash = params.remove("vnp_SecureHash");
        String calculatedHash = VNPayConfig.hashAllFields(params);

        if (!calculatedHash.equals(secureHash)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Giao dịch không hợp lệ (Sai chữ ký).");
        }

        String responseCode = params.get("vnp_ResponseCode");
        if ("00".equals(responseCode)) {
            // Lưu thông tin thanh toán vào CSDL
            HoaDon hoaDon = new HoaDon();
            // Chuyển từ đơn vị nhỏ nhất (số tiền trả về từ VNPay là cent, chia cho 100 để có giá trị tiền tệ)
            Double totalAmount = Double.parseDouble(params.get("vnp_Amount")) / 100.0;
            hoaDon.setTongGia(totalAmount);
            hoaDon.setNgayTao(new Date());
            hoaDon.setTrangThai("Đã thanh toán");
            hoaDonRepository.save(hoaDon); // Lưu hóa đơn vào CSDL

            return ResponseEntity.ok("Thanh toán thành công!");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Giao dịch thất bại! Mã lỗi: " + responseCode);
        }
    }

}
