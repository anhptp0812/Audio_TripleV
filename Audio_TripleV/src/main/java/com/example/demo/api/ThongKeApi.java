package com.example.demo.api;

import com.example.demo.entity.ThongKe;
import com.example.demo.service.ThongKeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@RestController
@RequestMapping("/api/thong-ke")
public class ThongKeApi {
    @Autowired
    private ThongKeService thongKeService;

    @GetMapping("/ngay")
    public ResponseEntity<ThongKe> thongKeTheoNgay(@RequestParam String date) {
        try {
            // Sử dụng SimpleDateFormat để phân tích cú pháp chuỗi thành java.util.Date
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = dateFormat.parse(date); // chuyển chuỗi thành Date
            String trangThai = "Đã thanh toán";

            // Dùng Calendar để cộng thêm 1 ngày vào startDate để lấy endDate
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            calendar.add(Calendar.DATE, 1); // Cộng thêm 1 ngày
            Date endDate = calendar.getTime();

            // Lấy dữ liệu thống kê theo ngày
            ThongKe thongKe = thongKeService.thongKeTheoNgay(startDate, endDate, trangThai);

            // Trả về dữ liệu thống kê
            return ResponseEntity.ok(thongKe);
        } catch (ParseException e) {
            // Trả về 400 nếu ngày không hợp lệ
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            // Trả về lỗi nội bộ nếu có lỗi khác
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/thang")
    public ResponseEntity<ThongKe> thongKeTheoThang(@RequestParam int month, @RequestParam int year) {
        // Chuyển đổi tháng, năm thành ngày đầu và cuối của tháng
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);  // Đặt ngày đầu của tháng
        Date startDate = calendar.getTime();
        calendar.set(year, month - 1, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));  // Đặt ngày cuối của tháng
        Date endDate = calendar.getTime();
        String trangThai = "Đã thanh toán";

        ThongKe thongKe = thongKeService.thongKeTheoThang(startDate, endDate, trangThai); // Gửi startDate và endDate
        return ResponseEntity.ok(thongKe);
    }

    @GetMapping("/nam")
    public ResponseEntity<ThongKe> thongKeTheoNam(@RequestParam int year) {
        // Tính toán ngày bắt đầu và kết thúc trong năm
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, Calendar.JANUARY, 1);  // Đặt ngày bắt đầu của năm
        Date startDate = calendar.getTime();
        calendar.set(year, Calendar.DECEMBER, 31);  // Đặt ngày cuối của năm
        Date endDate = calendar.getTime();
        String trangThai = "Đã thanh toán";

        ThongKe thongKe = thongKeService.thongKeTheoNam(startDate, endDate, trangThai);  // Gửi startDate và endDate
        return ResponseEntity.ok(thongKe);
    }

}
