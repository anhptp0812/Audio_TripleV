package com.example.demo.controller;

import com.example.demo.entity.*;
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

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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


//    @GetMapping("/don-hang")
//    public String index(Model model) {
//        model.addAttribute("donHang", new HoaDon());
//        List<HoaDon> list = hoaDonRepository.findAll();
//        model.addAttribute("listDH", list);
//        return "nhanvien/donhang";
//
//    }

    @GetMapping("/don-hang")
    public String index(Model model) {
        model.addAttribute("donHang", new DonHang());
        List<DonHang> list = donHangRepository.findAll();
        model.addAttribute("listDH", list);

        List<String> trangThaiOptions = List.of("Chờ xử lý", "Đã xác nhận", "Đang giao", "Đã giao hàng", "Đã hủy");
        model.addAttribute("trangThaiOptions", trangThaiOptions);

        return "nhanvien/donhang";
    }

    @PostMapping("/don-hang/cap-nhat-trang-thai")
    public String capNhatTrangThai(@RequestParam("id") Integer id, @RequestParam("trangThai") String trangThai) {
        DonHang donHang = donHangRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));
        donHang.setTrangThai(trangThai);
        donHangRepository.save(donHang);
        return "redirect:/user/don-hang"; // Quay lại trang danh sách đơn hàng
    }

    @PostMapping("/ban-hang/{id}")
    public String saveOrderDetails(
            @PathVariable Integer id,
            @RequestParam(required = false, value = "spctIds") List<Integer> spctIds,
            @RequestParam(required = false, value = "soLuong") List<Integer> quantities,
            @RequestParam("paymentMethod") String paymentMethod,
            Model model) {

        HoaDon hoaDon = hoaDonService.findByid(id);
        if (hoaDon == null) {
            model.addAttribute("error", "Đơn hàng không tồn tại!");
            return "error";
        }

        Integer nhanVienId = nhanVienService.getLoggedInNhanVienId();
        Optional<NhanVien> nhanVienOptional = nhanVienRepo.findById(nhanVienId);

        if (nhanVienOptional.isPresent()) {
            hoaDon.setNhanVien(nhanVienOptional.get());
        } else {
            model.addAttribute("error", "Không tìm thấy thông tin nhân viên!");
            return "error";
        }

        switch (paymentMethod) {
            case "xacnhan":
                return handleConfirmation(hoaDon, spctIds, quantities, model);

            case "cash":
                return handleCashPayment(hoaDon, spctIds, quantities, model);

            case "vnpay":
                hoaDon.setTrangThai("Chờ thanh toán VNPay");
                hoaDonRepository.save(hoaDon);
                return "redirect:/vnpay-hien-thi/" + hoaDon.getId();

            case "xoa":
                return handleDeletion(hoaDon, model);

            default:
                model.addAttribute("error", "Hành động không hợp lệ!");
                return "error";
        }
    }

    private String handleConfirmation(HoaDon hoaDon, List<Integer> spctIds, List<Integer> quantities, Model model) {
        if (spctIds == null || quantities == null) {
            model.addAttribute("error", "Danh sách sản phẩm hoặc số lượng không hợp lệ!");
            return "error";
        }

        double totalAmount = hoaDon.getTongGia() == null ? 0 : hoaDon.getTongGia();

        for (int i = 0; i < spctIds.size(); i++) {
            Integer spctId = spctIds.get(i);
            Integer soLuong = quantities.get(i);

            if (!updateProductAndCreateDetail(hoaDon, spctId, soLuong, model)) {
                return "error";
            }

            totalAmount += sanPhamChiTietService.findById(spctId).getDonGia() * soLuong;
        }

        hoaDon.setTongGia(totalAmount);
        hoaDon.setTrangThai("Chờ thanh toán");
        hoaDonRepository.save(hoaDon);

        return "redirect:/user/ban-hang/" + hoaDon.getId();
    }

    private String handleCashPayment(HoaDon hoaDon, List<Integer> spctIds, List<Integer> quantities, Model model) {
        if ("Đã thanh toán".equals(hoaDon.getTrangThai())) {
            model.addAttribute("error", "Hóa đơn đã được thanh toán!");
            return "error";
        }

        if (spctIds != null && quantities != null) {
            for (int i = 0; i < spctIds.size(); i++) {
                Integer spctId = spctIds.get(i);
                Integer soLuong = quantities.get(i);

                if (!updateProductAndCreateDetail(hoaDon, spctId, soLuong, model)) {
                    return "error";
                }
            }
        }

        hoaDon.setTrangThai("Đã thanh toán");
        hoaDonRepository.save(hoaDon);
        return "redirect:/user/ban-hang";
    }

    private boolean updateProductAndCreateDetail(HoaDon hoaDon, Integer spctId, Integer soLuong, Model model) {
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietService.findById(spctId);
        if (sanPhamChiTiet == null || sanPhamChiTiet.getSoLuong() < soLuong) {
            model.addAttribute("error", "Sản phẩm " + (sanPhamChiTiet != null ? sanPhamChiTiet.getSanPham().getTen() : "không xác định") + " không đủ số lượng.");
            return false;
        }

        HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();
        hoaDonChiTiet.setHoaDon(hoaDon);
        hoaDonChiTiet.setSanPhamChiTiet(sanPhamChiTiet);
        hoaDonChiTiet.setDonGia(sanPhamChiTiet.getDonGia());
        hoaDonChiTiet.setSoLuong(soLuong);
        hoaDonChiTiet.setNgayTao(new Date());
        hoaDonChiTiet.setNgayCapNhat(new Date());
        hoaDonChiTietRepository.save(hoaDonChiTiet);

        sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() - soLuong);
        sanPhamChiTietRepository.save(sanPhamChiTiet);

        return true;
    }

    private String handleDeletion(HoaDon hoaDon, Model model) {
        List<HoaDonChiTiet> hoaDonChiTietList = hoaDonChiTietRepository.findByHoaDon_Id(hoaDon.getId());
        if (hoaDonChiTietList.isEmpty()) {
            model.addAttribute("error", "Không có chi tiết hóa đơn để xóa!");
            return "error";
        }

        for (HoaDonChiTiet hoaDonChiTiet : hoaDonChiTietList) {
            SanPhamChiTiet sanPhamChiTiet = hoaDonChiTiet.getSanPhamChiTiet();
            sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() + hoaDonChiTiet.getSoLuong());
            sanPhamChiTietRepository.save(sanPhamChiTiet);

            hoaDonChiTietRepository.deleteByHoaDon_Id(hoaDon.getId());
        }

        hoaDonRepository.delete(hoaDon);
        return "redirect:/user/ban-hang";
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
                          @RequestParam(required = false) String tenSanPham, // Thêm tham số tên sản phẩm
                          @PathVariable Integer id, Model model) {
        HoaDon dh = hoaDonService.findByid(id);
        model.addAttribute("donHang", dh);
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
                (minPrice == null || maxPrice == null) &&
                (tenSanPham == null || tenSanPham.isEmpty())) {
            // Nếu không chọn bộ lọc nào thì lấy tất cả sản phẩm
            list = sanPhamChiTietRepository.findAll();

            for (SanPhamChiTiet spct : list) {
                // Sử dụng phương thức getFormattedDonGia() để lấy giá trị đã định dạng
                spct.getFormattedDonGia();
            }
        } else {
            // Lấy danh sách sản phẩm theo các bộ lọc
            list = sanPhamChiTietRepository.findByFilters(
                    idLoaiSP, idSanPham, mauSac, hang, minPrice, maxPrice, tenSanPham);
            for (SanPhamChiTiet spct : list) {
                // Sử dụng phương thức getFormattedDonGia() để lấy giá trị đã định dạng
                spct.getFormattedDonGia();
            }
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


