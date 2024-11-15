package com.example.demo.service;

import com.example.demo.vnPay.VNPayConfig;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class VNPayService {

        public String createVNPayUrl(HttpServletRequest req, double amount, String bankCode, Integer orderId) {
            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", "2.1.0");
            vnp_Params.put("vnp_Command", "pay");
            vnp_Params.put("vnp_TmnCode", VNPayConfig.vnp_TmnCode);
            vnp_Params.put("vnp_Amount", String.valueOf(amount * 100)); // Chuyển thành đơn vị nhỏ nhất
            vnp_Params.put("vnp_CurrCode", "VND");
            vnp_Params.put("vnp_TxnRef", VNPayConfig.getRandomNumber(8)); // Mã giao dịch duy nhất
            vnp_Params.put("vnp_OrderInfo", "Thanh toán đơn hàng " + orderId);
            vnp_Params.put("vnp_OrderType", "billpayment");
            vnp_Params.put("vnp_ReturnUrl", VNPayConfig.vnp_ReturnUrl);
            vnp_Params.put("vnp_IpAddr", VNPayConfig.getIpAddress(req));
            vnp_Params.put("vnp_Locale", "vn");
            vnp_Params.put("vnp_CreateDate", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));

            if (bankCode != null) {
                vnp_Params.put("vnp_Amount", String.valueOf((long)(amount * 100)));

            }

            String secureHash = VNPayConfig.hashAllFields(vnp_Params);
            vnp_Params.put("vnp_SecureHash", secureHash);
            System.out.println(vnp_Params); // In ra các tham số

            String vnpayUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html?vnp_TmnCode=your_vnp_TmnCode&vnp_Amount=totalAmount&...";
            String queryUrl = vnp_Params.entrySet().stream()
                    .map(e -> e.getKey() + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
                    .collect(Collectors.joining("&"));
            System.out.println("URL gửi đi: " + VNPayConfig.vnp_PayUrl + "?" + queryUrl);

            return VNPayConfig.vnp_PayUrl + "?" + queryUrl;
        }
}