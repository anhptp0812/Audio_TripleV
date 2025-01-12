package com.example.demo.service;

import com.example.demo.entity.ThongKe;
import com.example.demo.repository.HoaDonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;

@Service
public class ThongKeService {

    @Autowired
    private HoaDonRepository hoaDonRepository;

    public ThongKe thongKeTheoNgay(Date startDate, Date endDate) {
        ThongKe thongKe = new ThongKe();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = dateFormat.format(startDate);

        // Gán giá trị đã định dạng cho thuộc tính thoiGian
        thongKe.setThoiGian(formattedDate);

        // Lấy tổng doanh thu cho ngày cụ thể
        thongKe.setTongDoanhThu(hoaDonRepository.getTongDoanhThuTheoNgay(startDate, endDate));
        // Lấy số lượng đơn hàng trong ngày
        thongKe.setSoLuongDonHang(hoaDonRepository.countByNgayTaoBetween(startDate, endDate));
        // Tính lợi nhuận giả định (20% doanh thu)
        thongKe.setLoiNhuan(tinhLoiNhuan(thongKe.getTongDoanhThu()));
        return thongKe;
    }

    public ThongKe thongKeTheoThang(Date startDate, Date endDate) {
        ThongKe thongKe = new ThongKe();

        // Sử dụng Calendar để lấy tháng và năm từ startDate
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        // Định dạng thời gian theo kiểu "MM/yyyy"
        int month = calendar.get(Calendar.MONTH) + 1; // Tháng từ 0-11, cần cộng thêm 1
        int year = calendar.get(Calendar.YEAR); // Lấy năm

        // Định dạng thoiGian theo "MM/yyyy"
        thongKe.setThoiGian(String.format("%02d/%d", month, year));  // Đảm bảo tháng có 2 chữ số

        // Lấy tổng doanh thu cho tháng
        thongKe.setTongDoanhThu(hoaDonRepository.getTongDoanhThuTheoThang(startDate, endDate));

        // Lấy số lượng đơn hàng trong tháng
        thongKe.setSoLuongDonHang(hoaDonRepository.countByNgayTaoBetween(startDate, endDate));

        // Tính lợi nhuận giả định (20% doanh thu)
        thongKe.setLoiNhuan(tinhLoiNhuan(thongKe.getTongDoanhThu()));

        return thongKe;
    }


    public ThongKe thongKeTheoNam(Date startDate, Date endDate) {
        ThongKe thongKe = new ThongKe();
        thongKe.setThoiGian(String.valueOf(startDate.getYear() + 1900));  // Năm
        thongKe.setTongDoanhThu(hoaDonRepository.getTongDoanhThuTheoNam(startDate, endDate));
        thongKe.setSoLuongDonHang(hoaDonRepository.countByNgayTaoBetween(startDate, endDate));
        thongKe.setLoiNhuan(tinhLoiNhuan(thongKe.getTongDoanhThu()));
        return thongKe;
    }

    private double tinhLoiNhuan(Double tongDoanhThu) {
        // Giả định lợi nhuận là 20% tổng doanh thu
        return tongDoanhThu != null ? tongDoanhThu * 0.2 : 0.0;
    }
}
