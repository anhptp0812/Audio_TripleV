package com.example.demo.api;

import com.example.demo.entity.DonHang;
import com.example.demo.entity.DonHangChiTiet;
import com.example.demo.entity.KhachHang;
import com.example.demo.entity.SanPhamChiTiet;
import com.example.demo.repository.KhachHangRepo;
import com.example.demo.repository.LoaiSanPhamRepository;
import com.example.demo.repository.SanPhamChiTietRepository;
import com.example.demo.service.KhachHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
//@RestController
public class NhanVienController {
    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;

    @Autowired
    private  KhachHangService khachHangService;

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

        List<SanPhamChiTiet> list = sanPhamChiTietRepository.findAll();
        model.addAttribute("spct", list);

        List<KhachHang> list1 = khachHangRepo.findAll();
        model.addAttribute("listKh", list1);

        model.addAttribute("donHang", new DonHang());
        model.addAttribute("donHangChiTiet", new DonHangChiTiet());



        return "nhanvien/productProvity";
    }


}
