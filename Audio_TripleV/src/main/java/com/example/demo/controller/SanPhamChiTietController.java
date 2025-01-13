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
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/admin/spct")
public class SanPhamChiTietController {

    @Autowired
    private SanPhamRepository sanPhamRepository;

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

//    @GetMapping("/hien-thi")
//    public String hienThi(Model model) {
//        List<SanPhamChiTiet> spctList = sanPhamChiTietRepository.findAll();
//
//        for (SanPhamChiTiet spct : spctList) {
//            // Sử dụng phương thức getFormattedDonGia() để lấy giá trị đã định dạng
//            spct.getFormattedDonGia();
//        }
//
//        model.addAttribute("spct", spctList);
//        model.addAttribute("mausac", mauSacRepository.findAll());
//        model.addAttribute("hang", hangRepository.findAll());
//        model.addAttribute("hinhAnhs", hinhAnhRepository.findAll());
//        model.addAttribute("loaisp", loaiSanPhamRepository.findAll());
//        return "admin/quanlysanpham";
//    }

    @GetMapping("/hien-thi")
    public String hienThi(Model model) {
        List<SanPhamChiTiet> spctList = sanPhamChiTietRepository.findAll();

        for (SanPhamChiTiet spct : spctList) {
            // Sử dụng phương thức getFormattedDonGia() để lấy giá trị đã định dạng
            spct.getFormattedDonGia();
        }

        model.addAttribute("spct", spctList);
        return "admin/spct";
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
        return "redirect:/admin/spct/hien-thi";
    }

    @GetMapping("/form-add")
    public String add(Model model) {
        model.addAttribute("sanPhams", sanPhamRepository.findAll());
        model.addAttribute("mauSacs", mauSacRepository.findAll());
        model.addAttribute("hangs", hangRepository.findAll());
        model.addAttribute("hinhAnhs", hinhAnhRepository.findAll());
        model.addAttribute("loaiSanPhams", loaiSanPhamRepository.findAll());
        return "admin/spct-add";
    }

    @PostMapping(value = "/add", consumes = {"multipart/form-data"})
    public ResponseEntity<String> addSanPhamChiTiet(
            @RequestParam("sanPham") String sanPhamName,
            @RequestParam("mauSac") Integer mauSacId,
            @RequestParam("hang") Integer hangId,
            @RequestParam("hinhAnh") Integer hinhAnhId,
            @RequestParam("loaiSanPham") Integer loaiSanPhamId,
            @RequestParam("trangThai") String trangThai,
            @RequestParam("moTa") String moTa,
            @RequestParam("donGia") Double donGia,
            @RequestParam("soLuong") Integer soLuong) {

        if (donGia == null || donGia <= 1000) {
            return ResponseEntity.badRequest().body("Đơn giá phải lớn hơn 1000 và không được để trống.");
        }

//        if (soLuong == null || soLuong <= 0) {
//            return ResponseEntity.badRequest().body("Số lượng phải lớn hơn 0 và không được để trống.");
//        }

        try {
            SanPham sanPham = sanPhamRepository.findByTen(sanPhamName)
                    .orElseGet(() -> {
                        SanPham newSanPham = new SanPham();
                        newSanPham.setTen(sanPhamName);
                        newSanPham.setNgayTao(new Date());
                        newSanPham.setNgayCapNhat(new Date());
                        return sanPhamRepository.save(newSanPham);
                    });

            MauSac mauSac = mauSacRepository.findById(mauSacId)
                    .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy màu sắc"));
            Hang hang = hangRepository.findById(hangId)
                    .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy hãng"));
            LoaiSanPham loaiSanPham = loaiSanPhamRepository.findById(loaiSanPhamId)
                    .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy loại sản phẩm"));
            HinhAnh hinhAnh = hinhAnhRepository.findById(hinhAnhId)
                    .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy hình ảnh"));

            SanPhamChiTiet sanPhamChiTiet = new SanPhamChiTiet();
            sanPhamChiTiet.setSanPham(sanPham);
            sanPhamChiTiet.setMauSac(mauSac);
            sanPhamChiTiet.setHang(hang);
            sanPhamChiTiet.setHinhAnh(hinhAnh);
            sanPhamChiTiet.setLoaiSanPham(loaiSanPham);
            sanPhamChiTiet.setDonGia(donGia);
            sanPhamChiTiet.setSoLuong(soLuong);
            sanPhamChiTiet.setTrangThai(trangThai);
            sanPhamChiTiet.setMoTa(moTa);
            sanPhamChiTiet.setNgayTao(new Date());
            sanPhamChiTiet.setNgayCapNhat(new Date());

            sanPhamChiTietService.addSanPhamChiTiet(sanPhamChiTiet);

            return ResponseEntity.ok("Thêm sản phẩm chi tiết thành công!");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Có lỗi xảy ra: " + e.getMessage());
        }
    }

    @GetMapping("/form-update/{id}")
    public String update(@PathVariable Integer id, Model model) {
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietService.findById(id); // Tìm sản phẩm theo ID
        if (sanPhamChiTiet == null) {
            // Nếu sản phẩm không tồn tại, có thể chuyển hướng đến trang lỗi hoặc trang danh sách
            return "redirect:/admin/spct/error"; // Thay đổi đường dẫn đến trang lỗi hoặc trang danh sách sản phẩm
        }
        model.addAttribute("sanPhamChiTiet", sanPhamChiTiet); // Thêm sản phẩm vào model
        model.addAttribute("sanPhams", sanPhamRepository.findAll());
        model.addAttribute("mauSacs", mauSacRepository.findAll());
        model.addAttribute("hangs", hangRepository.findAll());
        model.addAttribute("hinhAnhs", hinhAnhRepository.findAll());
        model.addAttribute("loaiSanPhams", loaiSanPhamRepository.findAll());
        return "admin/spct-update"; // Trả về trang cập nhật
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateSanPhamChiTiet(
            @RequestParam("id") Integer id,
            @RequestParam("sanPham") String sanPhamName,
            @RequestParam("mauSac") Integer mauSacId,
            @RequestParam("hang") Integer hangId,
            @RequestParam("hinhAnh") Integer hinhAnhId,
            @RequestParam("loaiSanPham") Integer loaiSanPhamId,
            @RequestParam("trangThai") String trangThai,
            @RequestParam("moTa") String moTa,
            @RequestParam("donGia") Double donGia,
            @RequestParam("soLuong") Integer soLuong) {

        if (id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID sản phẩm chi tiết không hợp lệ.");
        }

// Kiểm tra nếu donGia và soLuong không hợp lệ
        if (donGia == null || donGia <= 1000) {
            return ResponseEntity.badRequest().body("Đơn giá phải lớn hơn 1000.");
        }

        if (soLuong == null || soLuong <= 0) {
            return ResponseEntity.badRequest().body("Số lượng phải lớn hơn 0.");
        }

        try {
            // Lấy đối tượng liên quan từ cơ sở dữ liệu
            SanPham sanPham = sanPhamRepository.findByTen(sanPhamName)
                    .orElseGet(() -> {
                        // Nếu sản phẩm không tồn tại, tạo sản phẩm mới và lưu vào cơ sở dữ liệu
                        SanPham newSanPham = new SanPham();
                        newSanPham.setTen(sanPhamName);  // Gán tên sản phẩm mới
                        newSanPham.setNgayTao(new Date());
                        newSanPham.setNgayCapNhat(new Date());
                        return sanPhamRepository.save(newSanPham);
                    });
            MauSac mauSac = mauSacRepository.findById(mauSacId)
                    .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy màu sắc"));
            Hang hang = hangRepository.findById(hangId)
                    .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy hãng"));
            HinhAnh hinhAnh = hinhAnhRepository.findById(hinhAnhId)
                    .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy hình ảnh"));
            LoaiSanPham loaiSanPham = loaiSanPhamRepository.findById(loaiSanPhamId)
                    .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy loại sản phẩm"));

            // Tạo hoặc cập nhật đối tượng SanPhamChiTiet
            SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy sản phẩm chi tiết"));

            sanPhamChiTiet.setSanPham(sanPham);
            sanPhamChiTiet.setMauSac(mauSac);
            sanPhamChiTiet.setHang(hang);
            sanPhamChiTiet.setHinhAnh(hinhAnh);
            sanPhamChiTiet.setLoaiSanPham(loaiSanPham);
            sanPhamChiTiet.setTrangThai(trangThai);
            sanPhamChiTiet.setMoTa(moTa);
            sanPhamChiTiet.setDonGia(donGia);
            sanPhamChiTiet.setSoLuong(soLuong);
            sanPhamChiTiet.setNgayCapNhat(new Date());

            // Lưu lại sản phẩm chi tiết đã cập nhật
            sanPhamChiTietService.updateSanPhamChiTiet(sanPhamChiTiet);

            return ResponseEntity.ok("Cập nhật sản phẩm chi tiết thành công!");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Có lỗi xảy ra: " + e.getMessage());
        }
    }
}