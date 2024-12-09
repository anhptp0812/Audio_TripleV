package com.example.demo.api;


import com.example.demo.entity.Hang;
import com.example.demo.entity.HoaDon;
import com.example.demo.entity.HoaDonChiTiet;
import com.example.demo.entity.KhachHang;
import com.example.demo.entity.LoaiSanPham;
import com.example.demo.entity.MauSac;
import com.example.demo.entity.SanPham;
import com.example.demo.entity.SanPhamChiTiet;
import com.example.demo.repository.HangRepository;
import com.example.demo.repository.HoaDonChiTietRepository;

import com.example.demo.repository.HoaDonRepository;
import com.example.demo.repository.KhachHangRepository;
import com.example.demo.repository.LoaiSanPhamRepository;
import com.example.demo.repository.MauSacRepository;
import com.example.demo.repository.NhanVienRepo;
import com.example.demo.repository.SanPhamChiTietRepository;
import com.example.demo.repository.SanPhamRepository;
import com.example.demo.service.DonHangService;
import com.example.demo.service.HoaDonService;
import com.example.demo.service.KhachHangService;
import com.example.demo.service.NhanVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
public class HoaDonController {
    @Autowired
    private DonHangService donHangService;

    @Autowired
    private NhanVienRepo nhanVienRepo;

    @Autowired
    SanPhamChiTietRepository sanPhamChiTietRepository;

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Autowired
    private HoaDonService hoaDonService;

    @Autowired
    private HoaDonChiTietRepository hoaDonChiTietRepository;

    @Autowired
    private KhachHangService khachHangService;

    @Autowired
    private NhanVienService nhanVienService;

    @Autowired
    private LoaiSanPhamRepository loaiSanPhamRepository;

    @Autowired
    private SanPhamRepository sanPhamRepository;

    @Autowired
    private MauSacRepository mauSacRepository;

    @Autowired
    private HangRepository hangRepository;

    @Autowired
    private KhachHangRepository khachHangRepository;

    @GetMapping("/hoa-don")
    public String index(Model model) {
        model.addAttribute("donHang", new HoaDon());
        List<HoaDon> list = hoaDonRepository.findAll();
        model.addAttribute("listDH", list);
        return "nhanvien/hoa-don";

    }

    @GetMapping("/ban-hang")
    public String banHang(@RequestParam(required = false) Integer idLoaiSP,
                          @RequestParam(required = false) Integer idSanPham,
                          @RequestParam(required = false) Integer mauSac,
                          @RequestParam(required = false) Integer hang,
                          @RequestParam(required = false) Double minPrice,
                          @RequestParam(required = false) Double maxPrice,
                          @RequestParam(required = false) String donGia, // Thêm tham số donGia
                          @RequestParam(required = false) String tenSanPham, // Thêm tham số tenSanPham
                          Model model) {

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
        } else {
            // Lấy danh sách sản phẩm theo các bộ lọc
            list = sanPhamChiTietRepository.findByFilters(
                    idLoaiSP, idSanPham, mauSac, hang, minPrice, maxPrice, tenSanPham);
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

        // Lấy danh sách khách hàng nếu cần
        List<KhachHang> listKhachHang = khachHangRepository.findAll();
        model.addAttribute("listKh", listKhachHang);

        // Truyền đối tượng DonHang vào model để có thể sử dụng trong form
        model.addAttribute("hoaDon", new HoaDon());
        model.addAttribute("hoaDonChiTiet", new HoaDonChiTiet());

        return "nhanvien/productProvity"; // Trả về trang sản phẩm
    }

    @GetMapping("/ban-hang/{hoaDonId}")
    public String donHang(@RequestParam(required = false) Integer idLoaiSP,
                          @RequestParam(required = false) Integer idSanPham,
                          @RequestParam(required = false) Integer mauSac,
                          @RequestParam(required = false) Integer hang,
                          @RequestParam(required = false) Double minPrice,
                          @RequestParam(required = false) Double maxPrice,
                          @RequestParam(required = false) String donGia, // Thêm tham số donGia
                          @RequestParam(required = false) String tenSanPham, // Thêm tham số tên sản phẩm
                          @PathVariable Integer hoaDonId, Model model) {
        HoaDon hoaDon = hoaDonService.findByid(hoaDonId);
        model.addAttribute("hoaDon", hoaDon);
        model.addAttribute("hoaDonChiTiet", new HoaDonChiTiet());

        List<SanPhamChiTiet> list;

        List<KhachHang> listKh = khachHangRepository.findAll();
        model.addAttribute("listKh", listKh);

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

    @PostMapping("/ban-hang/create")
    public ResponseEntity<Map<String, Object>> createHoaDon(
            @RequestParam String tenKhachHang,
            @RequestParam Integer idKhachHang,
            @RequestParam(required = false) Integer productId,
            @RequestParam(required = false) Integer quantity) {

        // Kiểm tra thông tin khách hàng
        if (tenKhachHang == null || tenKhachHang.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Tên của khách hàng là bắt buộc!"));
        }

        // Tìm khách hàng theo id
        KhachHang khachHang = khachHangRepository.findById(idKhachHang)
                .orElseThrow(() -> new RuntimeException("Khách hàng không tồn tại"));

        // Tạo hóa đơn mới
        HoaDon hoaDon = new HoaDon();
        hoaDon.setKhachHang(khachHang);
        hoaDon.setTrangThai("Chưa thanh toán");
        hoaDon.setNgayGiao(new Date());
        hoaDon.setNgayTao(new Date());
        hoaDonRepository.save(hoaDon);

        return ResponseEntity.ok(Map.of("id", hoaDon.getId()));
    }

    @GetMapping("/khach-hang/tim-kiem")
    public ResponseEntity<List<KhachHang>> timKiemKhachHang(
            @RequestParam(required = false) String ten,
            @RequestParam(required = false) String sdt) {

        List<KhachHang> listKhachHang;

        if ((ten == null || ten.trim().isEmpty()) && (sdt == null || sdt.trim().isEmpty())) {
            // Nếu không có điều kiện lọc, trả về toàn bộ khách hàng
            listKhachHang = khachHangRepository.findAll();
        } else if (ten == null || ten.trim().isEmpty()) {
            // Nếu chỉ có số điện thoại
            listKhachHang = khachHangRepository.findBySdtContaining(sdt.trim());
        } else if (sdt == null || sdt.trim().isEmpty()) {
            // Nếu chỉ có tên
            listKhachHang = khachHangRepository.findByTenContaining(ten.trim());
        } else {
            // Nếu cả tên và số điện thoại đều được cung cấp
            listKhachHang = khachHangRepository.findByTenContainingAndSdtContaining(
                    ten.trim(), sdt.trim());
        }

        return ResponseEntity.ok(listKhachHang);
    }

    @PostMapping("/khach-hang/them")
    public ResponseEntity<String> themKhachHang(
            @RequestParam String tenKhachHang,
            @RequestParam String soDienThoai) {

        // Validate tên
        if (tenKhachHang == null || tenKhachHang.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Tên không được để trống!");
        }
        if (!tenKhachHang.matches("[a-zA-ZÀ-ỹ\\s]+")) {
            return ResponseEntity.badRequest().body("Tên chỉ được chứa chữ cái và khoảng trắng!");
        }

        // Validate số điện thoại
        if (soDienThoai == null || soDienThoai.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Số điện thoại không được để trống!");
        }
        if (!soDienThoai.matches("\\d{10,11}")) {
            return ResponseEntity.badRequest().body("Số điện thoại phải từ 10-11 chữ số!");
        }

        // Kiểm tra khách hàng có tồn tại hay chưa
        Optional<KhachHang> existingKhachHang = khachHangRepository.findBySdt(soDienThoai);
        if (existingKhachHang.isPresent()) {
            return ResponseEntity.badRequest().body("Khách hàng với SĐT này đã tồn tại!");
        }

        // Tạo mới khách hàng
        KhachHang khachHang = new KhachHang();
        khachHang.setTen(tenKhachHang);
        khachHang.setSdt(soDienThoai);
        khachHangRepository.save(khachHang);

        return ResponseEntity.ok("Khách hàng đã được thêm thành công!");
    }

    @PostMapping("/ban-hang/{hoaDonId}/add-product")
    public ResponseEntity<Map<String, Object>> addProductToOrder(
            @PathVariable Integer hoaDonId,
            @RequestParam Integer spctId,
            @RequestParam Integer quantity) {

        if (spctId == null || spctId <= 0 || quantity == null || quantity <= 0) {
            throw new RuntimeException("Dữ liệu không hợp lệ: spctId hoặc quantity không hợp lệ");
        }

        HoaDon hoaDon = hoaDonRepository.findById(hoaDonId)
                .orElseThrow(() -> new RuntimeException("Hóa đơn không tồn tại"));

        // Kiểm tra sản phẩm
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(spctId)
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

        // Tìm HoaDonChiTiet
        // Kiểm tra sản phẩm đã tồn tại trong hóa đơn chưa
        Optional<HoaDonChiTiet> existingDetail = hoaDonChiTietRepository.findByHoaDonAndSanPhamChiTiet(hoaDon, sanPhamChiTiet);

        if (existingDetail.isPresent()) {
            // Nếu sản phẩm đã tồn tại, tăng số lượng
            HoaDonChiTiet hoaDonChiTiet = existingDetail.get();
            hoaDonChiTiet.setSoLuong(hoaDonChiTiet.getSoLuong() + quantity);
            hoaDonChiTiet.setNgayTao(new Date());

            // Lưu lại đối tượng đã cập nhật
            hoaDonChiTietRepository.save(hoaDonChiTiet);  // Quan trọng: Lưu lại vào database

            // Cập nhật tổng giá trị của hóa đơn
            double newTotal = (hoaDon.getTongGia() != null ? hoaDon.getTongGia() : 0)
                    + sanPhamChiTiet.getDonGia() * quantity;
            hoaDon.setTongGia(newTotal);
            hoaDonRepository.save(hoaDon);

            return ResponseEntity.ok(Map.of(
                    "id", hoaDonChiTiet.getId(),
                    "productName", sanPhamChiTiet.getSanPham().getTen(),
                    "quantity", hoaDonChiTiet.getSoLuong(),
                    "price", sanPhamChiTiet.getDonGia(),
                    "totalAmount", newTotal,
                    "isUpdated", true // Đánh dấu đây là bản ghi được cập nhật
            ));
        } else {
            // Nếu sản phẩm chưa có trong hóa đơn, tạo mới chi tiết hóa đơn
            HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();
            hoaDonChiTiet.setHoaDon(hoaDon);
            hoaDonChiTiet.setSanPhamChiTiet(sanPhamChiTiet);
            hoaDonChiTiet.setDonGia(sanPhamChiTiet.getDonGia());
            hoaDonChiTiet.setSoLuong(quantity);
            hoaDonChiTiet.setNgayTao(new Date());
            hoaDonChiTietRepository.save(hoaDonChiTiet);

            // Cập nhật tổng giá trị của hóa đơn
            double newTotal = (hoaDon.getTongGia() != null ? hoaDon.getTongGia() : 0)
                    + sanPhamChiTiet.getDonGia() * quantity;
            hoaDon.setTongGia(newTotal);
            hoaDonRepository.save(hoaDon);

            return ResponseEntity.ok(Map.of(
                    "id", hoaDonChiTiet.getId(),
                    "productName", sanPhamChiTiet.getSanPham().getTen(),
                    "quantity", quantity,
                    "price", sanPhamChiTiet.getDonGia(),
                    "totalAmount", newTotal,
                    "isUpdated", false // Đánh dấu đây là bản ghi mới
            ));
        }
    }

    @GetMapping("/ban-hang/{hoaDonId}/products")
    public ResponseEntity<List<Map<String, Object>>> getProductsInOrder(@PathVariable Integer hoaDonId) {
        HoaDon hoaDon = hoaDonRepository.findById(hoaDonId)
                .orElseThrow(() -> new RuntimeException("Hóa đơn không tồn tại"));

        List<HoaDonChiTiet> details = hoaDonChiTietRepository.findByHoaDon(hoaDon);

        // Sử dụng kiểu dữ liệu đơn giản cho Map
        List<Map<String, Object>> products = details.stream().map(detail -> {
            Map<String, Object> product = new HashMap<>();
            product.put("id", detail.getId());
            product.put("productName", detail.getSanPhamChiTiet().getSanPham().getTen());
            product.put("quantity", detail.getSoLuong());
            product.put("price", detail.getDonGia());
            product.put("totalPrice", detail.getDonGia() * detail.getSoLuong());
            return product;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(products);
    }

    @DeleteMapping("/ban-hang/{hoaDonId}/remove-product/{productId}")
    public ResponseEntity<Map<String, Object>> removeProductFromOrder(
            @PathVariable Integer hoaDonId,
            @PathVariable Integer productId) {

        HoaDon hoaDon = hoaDonRepository.findById(hoaDonId)
                .orElseThrow(() -> new RuntimeException("Hóa đơn không tồn tại"));

        HoaDonChiTiet detail = hoaDonChiTietRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại trong hóa đơn"));

        // Trừ giá trị sản phẩm khỏi tổng giá trị hóa đơn
        double updatedTotal = (hoaDon.getTongGia() != null ? hoaDon.getTongGia() : 0)
                - detail.getDonGia() * detail.getSoLuong();
        hoaDon.setTongGia(updatedTotal);
        hoaDonRepository.save(hoaDon);

        // Xóa sản phẩm khỏi hóa đơn
        hoaDonChiTietRepository.delete(detail);

        return ResponseEntity.ok(Map.of(
                "message", "Đã xóa sản phẩm thành công",
                "totalAmount", updatedTotal
        ));
    }

}

