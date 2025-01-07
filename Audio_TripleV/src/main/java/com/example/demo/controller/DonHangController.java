package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.repository.DonHangRepository;
import com.example.demo.repository.SanPhamChiTietRepository;
import com.example.demo.service.DonHangService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@CrossOrigin(origins = "http://localhost:3000") // Thay đổi URL này theo miền của frontend
@Controller
@RequestMapping("/user")
public class DonHangController {
    //    @Autowired
//    private VNPayConfig vnpayConfig;

    @Autowired
    private DonHangService donHangService;

    @Autowired
    private DonHangRepository donHangRepository;

    @GetMapping("/don-hang")
    public String index(@RequestParam(value = "trangThai", required = false) String trangThai, Model model) {
        List<DonHang> list;

        // Lọc đơn hàng theo trạng thái nếu có
        if (trangThai != null && !trangThai.isEmpty()) {
            list = donHangRepository.findByTrangThai(trangThai);
        } else {
            list = donHangRepository.findAll();
        }

        // Định dạng giá tiền thành VND
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        list.forEach(dh -> {
            String formattedPrice = currencyFormat.format(dh.getTongGia());
            dh.setFormattedTongGia(formattedPrice); // Đặt thêm thuộc tính để lưu giá định dạng
        });

        // Thông báo nếu không có đơn hàng phù hợp
        if (list.isEmpty()) {
            model.addAttribute("message", "Không có đơn hàng phù hợp với tiêu chí tìm kiếm.");
        }
        model.addAttribute("listDH", list);

        // Lựa chọn trạng thái có sẵn
        List<String> trangThaiOptions = List.of("Chờ xử lý", "Đã xác nhận", "Đang giao hàng", "Đã giao hàng", "Đã hủy");
        model.addAttribute("trangThaiOptions", trangThaiOptions);

        // Thêm đối tượng DonHang vào model
        model.addAttribute("dh", new DonHang()); // Thêm đối tượng DonHang rỗng vào model

        return "nhanvien/donhang"; // Trả về file HTML
    }

    @GetMapping("/don-hang/detail/{id}")
    public String viewOrderDetail(@PathVariable Integer id, Model model) {
        DonHang donHang = donHangService.findByid(id);
        if (donHang == null) {
            model.addAttribute("message", "Không tìm thấy đơn hàng với ID: " + id);
            return "error/404";
        }

        // Định dạng tổng giá và các chi tiết đơn hàng
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        // Định dạng tổng giá
        String formattedTongGia = currencyFormat.format(donHang.getTongGia());
        donHang.setFormattedTongGia(formattedTongGia);

        // Định dạng các chi tiết đơn hàng
        donHang.getDonHangChiTietList().forEach(chiTiet -> {
            // Định dạng đơn giá
            String formattedDonGia = currencyFormat.format(chiTiet.getDonGia());
            chiTiet.setFormattedDonGia(formattedDonGia);

            // Tính và định dạng thành tiền
            double thanhTien = chiTiet.getDonGia() * chiTiet.getSoLuong();
            String formattedThanhTien = currencyFormat.format(thanhTien);
            chiTiet.setFormattedThanhTien(formattedThanhTien);
        });

        model.addAttribute("donHang", donHang);
        return "nhanvien/detail-donhang";
    }

    @PostMapping("/don-hang/update-status")
    public String updateOrderStatus(@RequestParam Integer id, @RequestParam String status, RedirectAttributes redirectAttributes) {
        DonHang donHang = donHangService.findByid(id);
        if (donHang != null) {
            // Cập nhật trạng thái
            donHang.setTrangThai(status);

            // Cập nhật ngày cập nhật trạng thái
            donHang.setNgayCapNhat(new Date());

            // Nếu trạng thái là 'Đã giao hàng', cập nhật thêm ngày giao
            if ("Đã giao hàng".equals(status)) {
                donHang.setNgayGiao(new Date());
            }

            // Lưu lại thông tin thay đổi
            donHangService.save(donHang);

            // Thêm thông báo
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật trạng thái thành công!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Đơn hàng không tồn tại!");
        }
        return "redirect:/user/don-hang/detail/" + id;
    }


}