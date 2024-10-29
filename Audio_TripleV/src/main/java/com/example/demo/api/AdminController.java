package com.example.demo.api;

import com.example.demo.entity.NhanVien;
import com.example.demo.entity.SanPham;
import com.example.demo.repository.NhanVienRepo;
import com.example.demo.repository.SanPhamRepository;
import com.example.demo.service.NhanVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

//@RestController
@Controller
public class AdminController {

    @Autowired
    private NhanVienRepo nhanVienRepo;

    @Autowired
    private SanPhamRepository sanPhamRepository;

    private final NhanVienService nhanVienService;

    public AdminController(NhanVienService nhanVienService) {
        this.nhanVienService = nhanVienService;
    }

    //    @GetMapping("admin/nhan-vien")
//    public List<NhanVien> getNhanVien() {
//
//        return nhanVienService.layTatCaNhanVien();
//    }
    @GetMapping("/admin/nhan-vien")
    public String getNhanVien(Model model) {
        List<NhanVien> list = nhanVienService.layTatCaNhanVien();
        model.addAttribute("data", list);
        return "admin/nhan_vien"; // Trả về template HTML
    }

    @GetMapping("/admin/san-pham")
    public String getSanPham(Model model) {
        List<SanPham> list = sanPhamRepository.findAll();
        model.addAttribute("sp", list);
        return "admin/pro_duct"; // Trả về template HTML
    }

    @PostMapping("admin/san-pham/add")
    public String add() {
        return "";
    }

}
