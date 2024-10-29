package com.example.demo.api;

import com.example.demo.entity.DonHang;
import com.example.demo.entity.KhachHang;

import com.example.demo.entity.SanPhamChiTiet;
import com.example.demo.repository.KhachHangRepo;
import com.example.demo.repository.LoaiSanPhamRepository;
import com.example.demo.repository.SanPhamChiTietRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
//@RestController
public class NhanVienController {
    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;

    @Autowired
    private KhachHangRepo khachHangRepo;

    @Autowired
    private LoaiSanPhamRepository loaiSanPhamRepository;

//    @Autowired
//    private SanPhamChiTietService

    @GetMapping("user/trang-chu")
    public String index() {
        return "nhanvien/tin_tuc";
    }

    @GetMapping("user/ban-hang")
    public String banHang(Integer idLoaiSP, Model model) {
//        List<SanPhamChiTietCustom> list =  sanPhamChiTietRepository.findSanPhamChiTietByIdLoaiSP(idLoaiSP);
//        model.addAttribute("spct", list);
        List<SanPhamChiTiet> list = sanPhamChiTietRepository.findAll();
        model.addAttribute("spct", list);

//        List<SanPhamChiTietCustom> testList = sanPhamChiTietRepository.findSanPhamChiTietByIdLoaiSP(5);
//        System.out.println(testList); // Kiểm tra xem có dữ liệu không
        List<KhachHang> list1 = khachHangRepo.findAll();
        model.addAttribute("listKh", list1);

        model.addAttribute("donHang", new DonHang());

        return "nhanvien/productProvity";
    }

    @GetMapping("user/ban-hang/tim-kiem-kh")
    public ResponseEntity<List<KhachHang>> timKh(@RequestParam("ten") String ten, @RequestParam("sdt") String sdt) {
        List<KhachHang> khachHangList = khachHangRepo.findAllByTenAndSdt(ten, sdt);
        return ResponseEntity.ok(khachHangList);
    }
}
