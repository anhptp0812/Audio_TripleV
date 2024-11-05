package com.example.demo.controller;

import com.example.demo.entity.DonHang;
import com.example.demo.entity.DonHangChiTiet;
import com.example.demo.entity.SanPhamChiTiet;
import com.example.demo.repository.DonHangChiTietRepository;
import com.example.demo.entityCustom.DonHangRepository;
import com.example.demo.repository.SanPhamChiTietRepository;
import com.example.demo.service.DonHangService;
import com.example.demo.service.SanPhamChiTietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000") // Thay đổi URL này theo miền của frontend
@Controller
@RequestMapping("/user")
public class DonHangController {

    @Autowired
    private DonHangService donHangService;

    @Autowired
    private DonHangRepository donHangRepository;

    @Autowired
    private DonHangChiTietRepository donHangChiTietRepository;

    @Autowired
    SanPhamChiTietRepository sanPhamChiTietRepository;

    @Autowired
    private SanPhamChiTietService sanPhamChiTietService;

//    @GetMapping("ban-hang/don-hang/create")
//    public String createDonHangForm(Model model) {
//        model.addAttribute("donHang", new DonHang());
//        return "nhanvien/productProvity";  // Trả về form đơn hàng
//    }

    @GetMapping("/don-hang")
    public String index(Model model) {
        List<DonHang> list = donHangRepository.findAll();
        model.addAttribute("listDH", list);
        return "nhanvien/donhang";

    }

    @GetMapping("ban-hang/{id}")
    public String donHang(@PathVariable Integer id, Model model) {
        DonHang dh = donHangService.findByid(id);
        model.addAttribute("donHang", dh);
//        DonHangChiTiet dhct = donHangChiTietRepository.findByDonHang_Id(id).orElse(null);
        model.addAttribute("donHangChiTiet", new DonHangChiTiet());

        List<SanPhamChiTiet> list = sanPhamChiTietRepository.findAll();
        model.addAttribute("spct", list);
        return "nhanvien/productProvity";
    }

    //    @GetMapping("ban-hang/chi-tiet/{id}")
//    public String donHangChiTiet(@PathVariable Integer id, Model model) {
//        return "nhanvien/donhang";
//    }
    @PostMapping("ban-hang/save")
    public String saveDonHang(@ModelAttribute("donHang") DonHang donHang) {
        donHangRepository.save(donHang);

        return "redirect:/user/don-hang";
    }

//    @PostMapping( value ="/ban-hang/save-details", consumes = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseBody
//    public ResponseEntity<String> saveOrderDetails(@RequestBody OrderRequest orderRequest) {
//        // Lưu từng sản phẩm vào cơ sở dữ liệu
//
//        for (SanPhamChiTiet sanPhamChiTiet : orderRequest.getProducts()) {
//            DonHangChiTiet donHangChiTiet = new DonHangChiTiet();
//
//            // Tìm kiếm SanPhamChiTiet từ cơ sở dữ liệu dựa trên ID
//            SanPhamChiTiet existingProduct = sanPhamChiTietRepository.findById(sanPhamChiTiet.getId())
//                    .orElseThrow(() -> new RuntimeException("Product not found"));
//
//            donHangChiTiet.setSanPhamChiTiet(existingProduct); // Thiết lập đối tượng SanPhamChiTiet
//            donHangChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong()); // Số lượng
//       //     donHangChiTiet.setDonGia(sanPhamChiTiet.getDonGia()); // Giá
//            donHangChiTiet.setNgayTao(new Date()); // Ngày tạo
//            donHangChiTiet.setNgayCapNhat(new Date()); // Ngày tạo
//
//            // Lưu vào cơ sở dữ liệu
//            donHangChiTietRepository.save(donHangChiTiet);
//
//        }
//
//        return ResponseEntity.ok("Order saved successfully");
//    }

    @PostMapping("/ban-hang/{id}")
    public String saveOrderDetails(@PathVariable Integer id,
                                   @ModelAttribute("donHangChiTiet") DonHangChiTiet donHangChiTiet,
                                   Model model) {
        // Tìm đơn hàng theo ID
        DonHang donHang = donHangService.findByid(id);
        if (donHang == null) {
            model.addAttribute("error", "Đơn hàng không tồn tại!");
            return "error"; // Trả về trang lỗi nếu không tìm thấy đơn hàng
        }

        // Thiết lập thông tin cho donHangChiTiet
        donHangChiTiet.setDonHang(donHang);
        donHangChiTiet.setNgayTao(new Date()); // Ngày tạo
        donHangChiTiet.setNgayCapNhat(new Date()); // Ngày cập nhật

        // Lưu chi tiết đơn hàng vào cơ sở dữ liệu
        donHangChiTietRepository.save(donHangChiTiet);

        // Lấy danh sách sản phẩm chi tiết để hiển thị
        List<SanPhamChiTiet> list = sanPhamChiTietRepository.findAll();
        model.addAttribute("spct", list);

        // Chuyển hướng về trang danh sách đơn hàng
        return "redirect:/user/ban-hang";
    }

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


