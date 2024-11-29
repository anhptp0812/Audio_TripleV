package com.example.demo.controller;

import com.example.demo.entity.Hang;
import com.example.demo.entity.HinhAnh;
import com.example.demo.entity.LoaiSanPham;
import com.example.demo.entity.MauSac;
import com.example.demo.entity.SanPham;
import com.example.demo.entity.SanPhamChiTiet;
import com.example.demo.repository.HangRepository;
import com.example.demo.repository.HinhAnhRepository;
import com.example.demo.repository.LoaiSanPhamRepository;
import com.example.demo.repository.MauSacRepository;
import com.example.demo.repository.SanPhamChiTietRepository;
import com.example.demo.repository.SanPhamRepository;
import com.example.demo.service.SanPhamChiTietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;


@Controller
@RequestMapping("/spct")
public class SanPhamChiTietController {

    @Autowired
    SanPhamRepository sanPhamRepository;

    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;

    @Autowired
    private SanPhamChiTietService sanPhamChiTietService;

    @Autowired
    private MauSacRepository mauSacRepository;

    @Autowired
    private HangRepository hangRepository;

    @Autowired
    private HinhAnhRepository hinhAnhRepository;

    @Autowired
    private LoaiSanPhamRepository loaiSanPhamRepository;

    @GetMapping("/hien-thi")
    public String hienThi(Model model) {
        model.addAttribute("spct", sanPhamChiTietRepository.findAll());
        model.addAttribute("mausac", mauSacRepository.findAll());
        model.addAttribute("hang", hangRepository.findAll());
        model.addAttribute("loaisp", loaiSanPhamRepository.findAll());
        return "admin/quanlysanpham";
    }

    @GetMapping("/detail/{id}")
    public String getDetail(@PathVariable Integer id, Model model) {
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietService.findById(id);
        model.addAttribute("spct", sanPhamChiTiet);
        return "admin/spct-detail";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        sanPhamChiTietService.deleteById(id);
        return "redirect:/spct/hien-thi?activated=spct";
    }

    @GetMapping("/form-add")
    public String add() {
        return "admin/spct-add";
    }

    @PostMapping(value = "/add", consumes = "multipart/form-data")
    public ResponseEntity<String> addSanPhamChiTiet(
            @RequestParam("sanPham") Integer sanPhamId,
            @RequestParam("mauSac") Integer mauSacId,
            @RequestParam("hang") Integer hangId,
            @RequestParam("hinhAnh") Integer hinhAnhId,
            @RequestParam("loaiSanPham") Integer loaiSanPhamId,
            @RequestParam("trangThai") String trangThai) {

        try {
            // 1. Lấy các đối tượng liên quan
            SanPham sanPham = sanPhamRepository.findById(sanPhamId).orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));
            MauSac mauSac = mauSacRepository.findById(mauSacId).orElseThrow(() -> new RuntimeException("Không tìm thấy màu sắc"));
            Hang hang = hangRepository.findById(hangId).orElseThrow(() -> new RuntimeException("Không tìm thấy hãng"));
            HinhAnh hinhAnh = hinhAnhRepository.findById(hinhAnhId).orElseThrow(() -> new RuntimeException("Không tìm thấy Hinh Anh"));
            LoaiSanPham loaiSanPham = loaiSanPhamRepository.findById(loaiSanPhamId).orElseThrow(() -> new RuntimeException("Không tìm thấy loại sản phẩm"));

            // 3. Tạo đối tượng SanPhamChiTiet
            SanPhamChiTiet sanPhamChiTiet = new SanPhamChiTiet();
            sanPhamChiTiet.setSanPham(sanPham);
            sanPhamChiTiet.setMauSac(mauSac);
            sanPhamChiTiet.setHang(hang);
            sanPhamChiTiet.setHinhAnh(hinhAnh); // Tạo đối tượng HinhAnh
            sanPhamChiTiet.setLoaiSanPham(loaiSanPham);
            sanPhamChiTiet.setTrangThai(trangThai);
            sanPhamChiTiet.setNgayTao(new Date());
            sanPhamChiTiet.setNgayCapNhat(new Date());

            // 4. Lưu vào cơ sở dữ liệu
            sanPhamChiTietService.addSanPhamChiTiet(sanPhamChiTiet);

            return ResponseEntity.ok("Thêm sản phẩm chi tiết thành công!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Có lỗi xảy ra: " + e.getMessage());
        }
    }

    @GetMapping("/form-update/{id}")
    public String update(@PathVariable Integer id, Model model) {
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietService.findById(id); // Tìm sản phẩm theo ID
        if (sanPhamChiTiet == null) {
            // Nếu sản phẩm không tồn tại, có thể chuyển hướng đến trang lỗi hoặc trang danh sách
            return "redirect:/spct/error"; // Thay đổi đường dẫn đến trang lỗi hoặc trang danh sách sản phẩm
        }
        model.addAttribute("sanPhamChiTiet", sanPhamChiTiet); // Thêm sản phẩm vào model
        return "admin/spct-update"; // Trả về trang cập nhật
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateSanPhamChiTiet(@RequestBody SanPhamChiTiet sanPhamChiTiet) {
        try {
            SanPhamChiTiet updatedSanPhamChiTiet = sanPhamChiTietService.updateSanPhamChiTiet(sanPhamChiTiet);
            return ResponseEntity.ok(updatedSanPhamChiTiet);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi cập nhật sản phẩm chi tiết: " + e.getMessage());
        }
    }

}
