package com.example.demo.vnPay;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@Configuration
public class VNPayConfig {

    public static String vnp_PayUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    public static String vnp_ReturnUrl = "http://localhost:8080/vnpay_return";
    public static String vnp_TmnCode = "your_vnp_TmnCode";
    public static String secretKey = "your_secret_key";

    // Tạo mã giao dịch ngẫu nhiên
    public static String getRandomNumber(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append((int) (Math.random() * 10));
        }
        return sb.toString();
    }

    // Lấy địa chỉ IP của client
    public static String getIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }

    // Hàm hash dữ liệu cho chữ ký bảo mật
    public static String hashAllFields(Map<String, String> fields) {
        StringBuilder data = new StringBuilder();
        fields.entrySet().stream()
                .sorted(Map.Entry.comparingByKey()) // Sắp xếp theo key
                .forEach(entry -> data.append(entry.getKey()).append("=").append(entry.getValue()).append("&"));

        // Xóa ký tự `&` cuối cùng
        if (data.length() > 0) {
            data.setLength(data.length() - 1);
        }

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest((data.toString() + secretKey).getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Không tìm thấy thuật toán hash SHA-256", e);
        }
    }
}
