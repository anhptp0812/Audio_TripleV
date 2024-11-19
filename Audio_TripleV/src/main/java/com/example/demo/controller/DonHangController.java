package com.example.demo.controller;

import com.example.demo.entity.DonHangChiTiet;
import com.example.demo.entity.Hang;
import com.example.demo.entity.HoaDon;
import com.example.demo.entity.HoaDonChiTiet;
import com.example.demo.entity.LoaiSanPham;
import com.example.demo.entity.MauSac;
import com.example.demo.entity.SanPham;
import com.example.demo.entity.SanPhamChiTiet;
import com.example.demo.repository.DonHangChiTietRepository;
import com.example.demo.repository.DonHangRepository;
import com.example.demo.repository.HangRepository;
import com.example.demo.repository.HoaDonChiTietRepository;
import com.example.demo.repository.HoaDonRepository;
import com.example.demo.repository.LoaiSanPhamRepository;
import com.example.demo.repository.MauSacRepository;
import com.example.demo.repository.SanPhamChiTietRepository;
import com.example.demo.repository.SanPhamRepository;
import com.example.demo.service.DonHangService;
import com.example.demo.service.HoaDonService;
import com.example.demo.service.KhachHangService;
import com.example.demo.service.SanPhamChiTietService;
//import com.example.demo.service.VnPayService;
//import com.example.demo.vnPay.VNPayConfig;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;



import java.util.*;

import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000") // Thay đổi URL này theo miền của frontend
@RestController
@RequestMapping("/user")
public class DonHangController {
//    @Autowired
//    private VNPayConfig vnpayConfig;

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

//    @GetMapping("ban-hang/don-hang/create")
//    public String createDonHangForm(Model model) {
//        model.addAttribute("donHang", new DonHang());
//        return "nhanvien/productProvity";  // Trả về form đơn hàng
//    }


    @GetMapping("/don-hang")
    public String index(Model model) {
        List<HoaDon> list = hoaDonRepository.findAll();
        model.addAttribute("listDH", list);
        return "nhanvien/donhang";

    }

    @PostMapping("/ban-hang/{id}")
    public String saveOrderDetails(@PathVariable Integer id,
                                   @RequestParam("spctIds") List<Integer> spctIds,
                                   @RequestParam("soLuong") List<Integer> quantities,
                                   @RequestParam("paymentMethod") String paymentMethod,
                                   Model model, HttpServletRequest request
                                   ) {
        HoaDon hoaDon = hoaDonService.findByid(id);
        if (hoaDon == null) {
            model.addAttribute("error", "Đơn hàng không tồn tại!");
            return "error";
        }

        Double totalAmount = hoaDon.getTongGia();

        for (int i = 0; i < spctIds.size(); i++) {
            Integer spctId = spctIds.get(i);
            Integer soLuong = quantities.get(i);

            SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietService.findById(spctId);
            if (sanPhamChiTiet == null) continue;

            // Kiểm tra nếu số lượng tồn kho đủ để bán
            if (sanPhamChiTiet.getSoLuong() < soLuong) {
                model.addAttribute("error", "Số lượng không đủ cho sản phẩm " + sanPhamChiTiet.getSanPham().getTen());
                return "error";
            }

            // Tạo chi tiết đơn hàng
            HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();
            hoaDonChiTiet.setHoaDon(hoaDon);
            hoaDonChiTiet.setSanPhamChiTiet(sanPhamChiTiet);
            hoaDonChiTiet.setDonGia(sanPhamChiTiet.getDonGia());
            hoaDonChiTiet.setSoLuong(soLuong);
            hoaDonChiTiet.setNgayTao(new Date());
            hoaDonChiTiet.setNgayCapNhat(new Date());

            // Trừ số lượng từ SanPhamChiTiet và lưu lại
            sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() - soLuong);
            sanPhamChiTietRepository.save(sanPhamChiTiet); // Lưu số lượng cập nhật vào CSDL

            // Tính tổng cho đơn hàng
            double lineTotal = sanPhamChiTiet.getDonGia() * soLuong;
            totalAmount += lineTotal;

            // Lưu chi tiết đơn hàng

            // Thiết lập tổng giá cho hóa đơn
            hoaDon.setTongGia(totalAmount);
            // Lưu thông tin tổng giá của hóa đơn

            // Chuyển hướng đến trang VNPay để thanh toán
            if ("vnpay".equals(paymentMethod)) {
                hoaDonRepository.save(hoaDon);
                hoaDonChiTietRepository.save(hoaDonChiTiet);
                return "redirect:/vnpay-hien-thi/" + hoaDon.getId();
            } else if ("cash".equals(paymentMethod)) {
                // Xử lý thanh toán bằng tiền mặt
                hoaDon.setTrangThai("Đã thanh toán");
                hoaDonRepository.save(hoaDon);
                hoaDonChiTietRepository.save(hoaDonChiTiet);
                model.addAttribute("message", "Đơn hàng đã được ghi nhận. Vui lòng thanh toán tại cửa hàng!");
                return "redirect:/user/ban-hang"; // Chuyển hướng đến trang thông báo đơn hàng thành công

            }
        }
        return "error";
    }
    @GetMapping("ban-hang/details/{id}")
    @ResponseBody
    public List<DonHangChiTiet> getDonHangDetails(@PathVariable Integer id) {
        return donHangChiTietRepository.findByDonHang_Id(id);
    }

    @GetMapping("ban-hang/{id}")
    public String donHang(@RequestParam(required = false) Integer idLoaiSP,
                          @RequestParam(required = false) Integer idSanPham,
                          @RequestParam(required = false) Integer mauSac,
                          @RequestParam(required = false) Integer hang,
                          @RequestParam(required = false) Double minPrice,
                          @RequestParam(required = false) Double maxPrice,
                          @RequestParam(required = false) String donGia, // Thêm tham số donGia
                          @PathVariable Integer id, Model model) {
        HoaDon dh = hoaDonService.findByid(id);
        model.addAttribute("donHang", dh);
//        DonHangChiTiet dhct = donHangChiTietRepository.findByDonHang_Id(id).orElse(null);
        model.addAttribute("donHangChiTiet", new HoaDonChiTiet());

        List<SanPhamChiTiet> list;

        // Xử lý khoảng giá nếu có
        if (donGia != null && !donGia.isEmpty()) {
            String[] priceRange = donGia.split("-");
            if (priceRange.length == 2) {
                minPrice = Double.parseDouble(priceRange[0]);
                maxPrice = Double.parseDouble(priceRange[1]);
            } else if (priceRange.length == 1 && priceRange[0].endsWith("+")) {
                minPrice = Double.parseDouble(priceRange[0].replace("+", ""));
                maxPrice = Double.MAX_VALUE;
            }
        }

        // Kiểm tra các bộ lọc và lấy sản phẩm tương ứng
        if ((idLoaiSP == null || idLoaiSP == 0) &&
                (idSanPham == null || idSanPham == 0) &&
                (mauSac == null || mauSac == 0) &&
                (hang == null || hang == 0) &&
                (minPrice == null || maxPrice == null)) {
            // Nếu không chọn bộ lọc nào thì lấy tất cả sản phẩm
            list = sanPhamChiTietRepository.findAll();
        } else {
            // Lấy danh sách sản phẩm theo các bộ lọc
            list = sanPhamChiTietRepository.findByFilters(
                    idLoaiSP, idSanPham, mauSac, hang, minPrice, maxPrice);
        }

        // Thêm danh sách sản phẩm vào model
        model.addAttribute("spct", list);

        // Lấy tất cả loại sản phẩm, màu sắc, hãng để hiển thị trong form
        List<SanPham> sanPhams = sanPhamRepository.findAll();
        model.addAttribute("sanPhams", sanPhams);
        List<MauSac> mauSacs = mauSacRepository.findAll();
        model.addAttribute("mauSacs", mauSacs);
        List<Hang> hangs = hangRepository.findAll();
        model.addAttribute("hangs", hangs);
        List<LoaiSanPham> loaiSanPhams = loaiSanPhamRepository.findAll();
        model.addAttribute("loaiSanPhams", loaiSanPhams);

        return "nhanvien/productProvity"; // Trả về trang sản phẩm
    }


    @PostMapping("/ban-hang/delete/{dhctId}")
    public String deleteOrderDetail(@PathVariable Integer dhctId) {
        Optional<DonHangChiTiet> optionalDonHangChiTiet = donHangChiTietRepository.findById(dhctId);

        if (optionalDonHangChiTiet.isPresent()) {
            DonHangChiTiet donHangChiTiet = optionalDonHangChiTiet.get();
            SanPhamChiTiet sanPhamChiTiet = donHangChiTiet.getSanPhamChiTiet();

            // Tăng số lượng lại trong SanPhamChiTiet
            sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() + donHangChiTiet.getSoLuong());
            sanPhamChiTietRepository.save(sanPhamChiTiet);

            // Xóa chi tiết đơn hàng
            donHangChiTietRepository.delete(donHangChiTiet);
        }

        // Điều hướng lại đến trang chi tiết đơn hàng sau khi xóa
        return "redirect:/user/ban-hang"; // Hoặc trang bạn muốn quay lại sau khi xóa
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


