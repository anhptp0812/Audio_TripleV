package com.example.demo.controller;

import com.example.demo.entity.DonHangChiTiet;
import com.example.demo.entity.Hang;
import com.example.demo.entity.HoaDon;
import com.example.demo.entity.HoaDonChiTiet;
import com.example.demo.entity.LoaiSanPham;
import com.example.demo.entity.MauSac;
import com.example.demo.entity.NhanVien;
import com.example.demo.entity.SanPham;
import com.example.demo.entity.SanPhamChiTiet;
import com.example.demo.repository.DonHangChiTietRepository;
import com.example.demo.repository.DonHangRepository;
import com.example.demo.repository.HangRepository;
import com.example.demo.repository.HoaDonChiTietRepository;
import com.example.demo.repository.HoaDonRepository;
import com.example.demo.repository.LoaiSanPhamRepository;
import com.example.demo.repository.MauSacRepository;
import com.example.demo.repository.NhanVienRepo;
import com.example.demo.repository.SanPhamChiTietRepository;
import com.example.demo.repository.SanPhamRepository;
import com.example.demo.service.DonHangService;
import com.example.demo.service.HoaDonService;
import com.example.demo.service.KhachHangService;
import com.example.demo.service.NhanVienService;
import com.example.demo.service.SanPhamChiTietService;
//import com.example.demo.service.VnPayService;
//import com.example.demo.vnPay.VNPayConfig;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.*;

import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000") // Thay đổi URL này theo miền của frontend
@Controller
@RequestMapping("/user")
public class DonHangController {
//    @Autowired
//    private VNPayConfig vnpayConfig;
    @Autowired
    private NhanVienService nhanVienService;

    @Autowired
    private DonHangService donHangService;

    @Autowired
    private DonHangRepository donHangRepository;

    @Autowired
    private DonHangChiTietRepository donHangChiTietRepository;

    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;

    @Autowired
    private SanPhamChiTietService sanPhamChiTietService;

    @Autowired
    private LoaiSanPhamRepository loaiSanPhamRepository;

    @Autowired
    private SanPhamRepository sanPhamRepository;

    @Autowired
    private MauSacRepository mauSacRepository;

    @Autowired
    private HangRepository hangRepository;

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Autowired
    private HoaDonChiTietRepository hoaDonChiTietRepository;

    @Autowired
    private HoaDonService hoaDonService;

    @Autowired
    private KhachHangService khachHangService;

    @Autowired
    private NhanVienRepo nhanVienRepo;

//    @GetMapping("ban-hang/don-hang/create")
//    public String createDonHangForm(Model model) {
//        model.addAttribute("donHang", new DonHang());
//        return "nhanvien/productProvity";  // Trả về form đơn hàng
//    }

//
//    private String handleConfirmation(HoaDon hoaDon, List<Integer> spctIds, List<Integer> quantities, Model model) {
//        if (spctIds == null || quantities == null) {
//            model.addAttribute("error", "Danh sách sản phẩm hoặc số lượng không hợp lệ!");
//            return "error";
//        }
//
//        double totalAmount = hoaDon.getTongGia() == null ? 0 : hoaDon.getTongGia();
//
//        for (int i = 0; i < spctIds.size(); i++) {
//            Integer spctId = spctIds.get(i);
//            Integer soLuong = quantities.get(i);
//
//            if (!updateProductAndCreateDetail(hoaDon, spctId, soLuong, model)) {
//                return "error";
//            }
//
//            totalAmount += sanPhamChiTietService.findById(spctId).getDonGia() * soLuong;
//        }
//
//        hoaDon.setTongGia(totalAmount);
//        hoaDon.setTrangThai("Chờ thanh toán");
//        hoaDonRepository.save(hoaDon);
//
//        return "redirect:/user/ban-hang/" + hoaDon.getId();
//    }
//
//    private String handleCashPayment(HoaDon hoaDon, List<Integer> spctIds, List<Integer> quantities, Model model) {
//        if ("Đã thanh toán".equals(hoaDon.getTrangThai())) {
//            model.addAttribute("error", "Hóa đơn đã được thanh toán!");
//            return "error";
//        }
//
//        if (spctIds != null && quantities != null) {
//            for (int i = 0; i < spctIds.size(); i++) {
//                Integer spctId = spctIds.get(i);
//                Integer soLuong = quantities.get(i);
//
//                if (!updateProductAndCreateDetail(hoaDon, spctId, soLuong, model)) {
//                    return "error";
//                }
//            }
//        }
//
//        hoaDon.setTrangThai("Đã thanh toán");
//        hoaDonRepository.save(hoaDon);
//        return "redirect:/user/ban-hang";
//    }
//
//    private boolean updateProductAndCreateDetail(HoaDon hoaDon, Integer spctId, Integer soLuong, Model model) {
//        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietService.findById(spctId);
//        if (sanPhamChiTiet == null || sanPhamChiTiet.getSoLuong() < soLuong) {
//            model.addAttribute("error", "Sản phẩm " + (sanPhamChiTiet != null ? sanPhamChiTiet.getSanPham().getTen() : "không xác định") + " không đủ số lượng.");
//            return false;
//        }
//
//        HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();
//        hoaDonChiTiet.setHoaDon(hoaDon);
//        hoaDonChiTiet.setSanPhamChiTiet(sanPhamChiTiet);
//        hoaDonChiTiet.setDonGia(sanPhamChiTiet.getDonGia());
//        hoaDonChiTiet.setSoLuong(soLuong);
//        hoaDonChiTiet.setNgayTao(new Date());
//        hoaDonChiTiet.setNgayCapNhat(new Date());
//        hoaDonChiTietRepository.save(hoaDonChiTiet);
//
//        sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() - soLuong);
//        sanPhamChiTietRepository.save(sanPhamChiTiet);
//
//        return true;
//    }

//    private String handleDeletion(HoaDon hoaDon, Model model) {
//        List<HoaDonChiTiet> hoaDonChiTietList = hoaDonChiTietRepository.findByHoaDon_Id(hoaDon.getId());
//        if (hoaDonChiTietList.isEmpty()) {
//            model.addAttribute("error", "Không có chi tiết hóa đơn để xóa!");
//            return "error";
//        }
//
//        for (HoaDonChiTiet hoaDonChiTiet : hoaDonChiTietList) {
//            SanPhamChiTiet sanPhamChiTiet = hoaDonChiTiet.getSanPhamChiTiet();
//            sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() + hoaDonChiTiet.getSoLuong());
//            sanPhamChiTietRepository.save(sanPhamChiTiet);
//
//            hoaDonChiTietRepository.deleteByHoaDon_Id(hoaDon.getId());
//        }
//
//        hoaDonRepository.delete(hoaDon);
//        return "redirect:/user/ban-hang";
//    }
//
//
//    @GetMapping("ban-hang/details/{id}")
//    @ResponseBody
//    public List<DonHangChiTiet> getDonHangDetails(@PathVariable Integer id) {
//        return donHangChiTietRepository.findByDonHang_Id(id);
//    }
//

//
//    @PostMapping("/ban-hang/delete/{dhctId}")
//    public String deleteOrderDetail(@PathVariable Integer dhctId) {
//        Optional<DonHangChiTiet> optionalDonHangChiTiet = donHangChiTietRepository.findById(dhctId);
//
//        if (optionalDonHangChiTiet.isPresent()) {
//            DonHangChiTiet donHangChiTiet = optionalDonHangChiTiet.get();
//            SanPhamChiTiet sanPhamChiTiet = donHangChiTiet.getSanPhamChiTiet();
//
//            // Tăng số lượng lại trong SanPhamChiTiet
//            sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() + donHangChiTiet.getSoLuong());
//            sanPhamChiTietRepository.save(sanPhamChiTiet);
//
//            // Xóa chi tiết đơn hàng
//            donHangChiTietRepository.delete(donHangChiTiet);
//        }
//
//        // Điều hướng lại đến trang chi tiết đơn hàng sau khi xóa
//        return "redirect:/user/ban-hang"; // Hoặc trang bạn muốn quay lại sau khi xóa
//    }

//    @DeleteMapping("/ban-hang/delete/{dhctId}")
//    public String deleteOrderDetail(@PathVariable Integer dhctId) {
//        Optional<DonHangChiTiet> optionalDonHangChiTiet = donHangChiTietRepository.findById(dhctId);
//
//        if (optionalDonHangChiTiet.isPresent()) {
//            DonHangChiTiet donHangChiTiet = optionalDonHangChiTiet.get();
//            SanPhamChiTiet sanPhamChiTiet = donHangChiTiet.getSanPhamChiTiet();
//
//            // Tăng số lượng lại trong SanPhamChiTiet
//            sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() + donHangChiTiet.getSoLuong());
//            sanPhamChiTietRepository.save(sanPhamChiTiet);
//
//            // Xóa chi tiết đơn hàng
//            donHangChiTietRepository.delete(donHangChiTiet);
//        }
//        // Điều hướng lại đến trang chi tiết đơn hàng sau khi xóa
//        return "redirect:/user/ban-hang"; // Hoặc trang bạn muốn quay lại sau khi xóa
//    }
//    @PostMapping("/process-payment")
//    public String processPayment(@RequestBody OrderRequest orderRequest) {
//        // Step 1: Create and save DonHang
//        DonHang donHang = new DonHang();
//       // donHang.setKhachHang(khachHangService.findById(orderRequest.getCustomerId()));
//        donHang.setTongGia(orderRequest.getTotalAmount());
//        donHang.setTrangThai("Đã thanh toán");
//        donHang.setNgayTao(new Date());
//        donHang.setNgayCapNhat(new Date());
//        donHang = donHangRepository.save(donHang);
//
//        // Step 2: Save DonHangChiTiet for each product
//        for (OrderProduct product : orderRequest.getProducts()) {
//            DonHangChiTiet donHangChiTiet = new DonHangChiTiet();
//            donHangChiTiet.setDonHang(donHang);
//            donHangChiTiet.setSanPhamChiTiet(sanPhamChiTietService.findById(product.getSanPhamChiTietId()));
//            donHangChiTiet.setSoLuong(product.getQuantity());
//            donHangChiTiet.setDonGia(product.getPrice());
//            donHangChiTiet.setNgayTao(new Date());
//            donHangChiTiet.setNgayCapNhat(new Date());
//
//            donHangChiTietRepository.save(donHangChiTiet);
//
//            // Step 3: Update product stock
//            sanPhamChiTietService.updateStock(product.getSanPhamChiTietId(), product.getQuantity());
//        }
//
//        return "Process Success";


}


