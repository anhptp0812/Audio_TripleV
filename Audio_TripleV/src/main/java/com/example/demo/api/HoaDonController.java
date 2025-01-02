package com.example.demo.api;


import com.example.demo.entity.CapNhatSoLuongRequest;
import com.example.demo.entity.GioHangChiTiet;
import com.example.demo.entity.Hang;
import com.example.demo.entity.HoaDon;
import com.example.demo.entity.HoaDonChiTiet;
import com.example.demo.entity.KhachHang;
import com.example.demo.entity.LoaiSanPham;
import com.example.demo.entity.MauSac;
import com.example.demo.entity.NhanVien;
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
import com.example.demo.service.SanPhamChiTietService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
    private SanPhamChiTietService sanPhamChiTietService;

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
    public String index(@RequestParam(value = "trangThai", required = false) String trangThai, Model model) {
        List<HoaDon> list;

        // Lọc theo trạng thái nếu có
        if (trangThai != null && !trangThai.isEmpty()) {
            list = hoaDonRepository.findByTrangThai(trangThai);
        } else {
            list = hoaDonRepository.findAll();
        }

        // Định dạng tiền tệ cho các giá trị trong HoaDon
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        for (HoaDon hoaDon : list) {
            // Kiểm tra nếu tongGia khác null mới định dạng
            if (hoaDon.getTongGia() != null) {
                hoaDon.setFormattedTongGia(currencyFormat.format(hoaDon.getTongGia()));
            } else {
                hoaDon.setFormattedTongGia(currencyFormat.format(0.0)); // Thay thế với giá trị mặc định nếu null
            }

            // Làm tương tự với các trường khác nếu cần
            if (hoaDon.getTienKhachDua() != null) {
                hoaDon.setFormattedTienKhachDua(currencyFormat.format(hoaDon.getTienKhachDua()));
            } else {
                hoaDon.setFormattedTienKhachDua(currencyFormat.format(0.0));
            }

            if (hoaDon.getTienThua() != null) {
                hoaDon.setFormattedTienThua(currencyFormat.format(hoaDon.getTienThua()));
            } else {
                hoaDon.setFormattedTienThua(currencyFormat.format(0.0));
            }
        }

        model.addAttribute("listHD", list);
        return "nhanvien/hoa-don";
    }

    @GetMapping("/hoa-don-detail/{id}")
    public String viewHoaDonDetail(@PathVariable Integer id, Model model) {
        HoaDon hoaDon = hoaDonService.findById(id);
        if (hoaDon != null) {
            System.out.println("Hoa Don Found: " + hoaDon.getId());  // Kiểm tra thông tin của hoaDon
            // Định dạng giá trị tiền tệ
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

            // Định dạng các giá trị trong hoaDon
            hoaDon.setFormattedTongGia(currencyFormat.format(hoaDon.getTongGia()));
            hoaDon.setFormattedTienKhachDua(currencyFormat.format(hoaDon.getTienKhachDua()));
            hoaDon.setFormattedTienThua(currencyFormat.format(hoaDon.getTienThua()));

            // Định dạng giá trị cho từng chi tiết hóa đơn
            for (HoaDonChiTiet hdct : hoaDon.getHoaDonChiTietList()) {
                hdct.setFormattedDonGia(currencyFormat.format(hdct.getDonGia()));
                hdct.setFormattedTongGia(currencyFormat.format(hdct.getTongGia()));
            }

            // Lấy danh sách hóa đơn từ repository
            List<HoaDon> list = hoaDonRepository.findAll();

            model.addAttribute("hoaDon", hoaDon);
            model.addAttribute("hoaDonList", list); // Truyền danh sách hóa đơn vào model nếu cần
            return "nhanvien/hoa-don-detail"; // Trả về trang chi tiết hóa đơn
        }
        return "redirect:/user/hoa-don"; // Nếu không tìm thấy hóa đơn, quay lại danh sách hóa đơn
    }
    
    @GetMapping("/ban-hang")
    public String banHang(@RequestParam(required = false) Integer idLoaiSP,
                          @RequestParam(required = false) Integer idSanPham,
                          @RequestParam(required = false) Integer mauSac,
                          @RequestParam(required = false) Integer hang,
                          @RequestParam(required = false) Double minPrice,
                          @RequestParam(required = false) Double maxPrice,
                          @RequestParam(required = false) String donGia,
                          @RequestParam(required = false) String tenSanPham,
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
            list = sanPhamChiTietRepository.findAll();
        } else {
            list = sanPhamChiTietRepository.findByFilters(
                    idLoaiSP, idSanPham, mauSac, hang, minPrice, maxPrice, tenSanPham);
        }

        // Thêm trạng thái sản phẩm dựa trên số lượng
        for (SanPhamChiTiet spct : list) {
            if (spct.getSoLuong() == 0) {
                spct.setTrangThai("Hết hàng");
            } else {
                spct.setTrangThai("Còn hàng");
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

        // Lấy danh sách khách hàng nếu cần
        List<KhachHang> listKhachHang = khachHangRepository.findAll();
        model.addAttribute("listKh", listKhachHang);

        // Truyền đối tượng DonHang vào model để có thể sử dụng trong form
        model.addAttribute("hoaDon", new HoaDon());
        model.addAttribute("hoaDonChiTiet", new HoaDonChiTiet());

        return "nhanvien/productProvity";
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

        Integer nhanVienId = nhanVienService.getLoggedInNhanVienId();
        Optional<NhanVien> nhanVienOptional = nhanVienRepo.findById(nhanVienId);
        // Kiểm tra thông tin khách hàng
        if (tenKhachHang == null || tenKhachHang.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Tên của khách hàng là bắt buộc!"));
        }

        // Tìm khách hàng theo id
        KhachHang khachHang = khachHangRepository.findById(idKhachHang)
                .orElseThrow(() -> new RuntimeException("Khách hàng không tồn tại"));

        // Tạo hóa đơn mới
        if (nhanVienOptional.isPresent()) {
            HoaDon hoaDon = new HoaDon();
            hoaDon.setKhachHang(khachHang);
            hoaDon.setNhanVien(nhanVienOptional.get()); // Gán nhân viên vào hóa đơn
            hoaDon.setTrangThai("Chưa thanh toán");
            hoaDon.setNgayTao(new Date());
            hoaDonRepository.save(hoaDon);

            return ResponseEntity.ok(Map.of("id", hoaDon.getId()));
        }
        return null;
    }

    @GetMapping("/khach-hang/tim-kiem")
    @ResponseBody
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
            @RequestParam Integer soLuong) {

        // Kiểm tra dữ liệu đầu vào
        if (spctId == null || spctId <= 0 || soLuong == null || soLuong <= 0) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Dữ liệu không hợp lệ: spctId hoặc số lượng không hợp lệ."));
        }

        // Lấy nhân viên hiện tại
        Integer nhanVienId = nhanVienService.getLoggedInNhanVienId();
        Optional<NhanVien> nhanVienOptional = nhanVienRepo.findById(nhanVienId);

        if (nhanVienOptional.isPresent()) {
            HoaDon hoaDon = hoaDonRepository.findById(hoaDonId)
                    .orElse(new HoaDon());  // Nếu không tìm thấy hóa đơn, tạo mới hóa đơn

            // Nếu hóa đơn mới, thêm thông tin nhân viên vào hóa đơn
            hoaDon.setNhanVien(nhanVienOptional.get());  // Thêm nhân viên vào hóa đơn
            hoaDonRepository.save(hoaDon);

            // Kiểm tra sản phẩm
            SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(spctId)
                    .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

            // Kiểm tra số lượng trong kho
            if (sanPhamChiTiet.getSoLuong() <= 0) {
                return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Sản phẩm đã hết hàng."));
            }
            if (sanPhamChiTiet.getSoLuong() < soLuong) {
                return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Không đủ số lượng sản phẩm trong kho."));
            }

            // Tìm HoaDonChiTiet nếu đã tồn tại
            Optional<HoaDonChiTiet> existingDetail = hoaDonChiTietRepository.findByHoaDonAndSanPhamChiTiet(hoaDon, sanPhamChiTiet);

            if (existingDetail.isPresent()) {
                return ResponseEntity.ok(Collections.singletonMap("message", "Sản phẩm đã có trong giỏ hàng!"));
            } else {
                // Nếu sản phẩm chưa có trong hóa đơn, thêm mới
                HoaDonChiTiet newDetail = new HoaDonChiTiet();
                newDetail.setHoaDon(hoaDon);
                newDetail.setSanPhamChiTiet(sanPhamChiTiet);
                newDetail.setSoLuong(soLuong);
                newDetail.setDonGia(sanPhamChiTiet.getDonGia());
                hoaDonChiTietRepository.save(newDetail);

                // Giảm số lượng trong kho
                sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() - soLuong);
                sanPhamChiTietRepository.save(sanPhamChiTiet);

                // Cập nhật tổng giá hóa đơn
                double newTotal = (hoaDon.getTongGia() != null ? hoaDon.getTongGia() : 0)
                        + sanPhamChiTiet.getDonGia() * soLuong;
                hoaDon.setTongGia(newTotal);
                hoaDonRepository.save(hoaDon);

                return ResponseEntity.ok(Map.of(
                        "id", sanPhamChiTiet.getId(),
                        "productName", sanPhamChiTiet.getSanPham().getTen(),
                        "quantity", soLuong,
                        "price", sanPhamChiTiet.getDonGia(),
                        "totalAmount", newTotal,
                        "remainingQuantity", sanPhamChiTiet.getSoLuong(),
                        "isUpdated", false, // Bản ghi mới
                        "message", "Sản phẩm mới đã được thêm vào hóa đơn."
                ));
            }
        }
        return null;
    }

    @PostMapping("/cap-nhat-so-luong")
    @ResponseBody
    public ResponseEntity<?> capNhatSoLuong(@RequestBody CapNhatSoLuongRequest request) {
        try {
            // Lấy thông tin sản phẩm chi tiết
            SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietService.findById(request.getProductId());
            if (sanPhamChiTiet == null) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Sản phẩm không tồn tại."));
            }

            // Lấy hóa đơn chi tiết liên quan đến sản phẩm
            HoaDonChiTiet hoaDonChiTiet = hoaDonService.findHoaDonChiTietByProductId(request.getProductId());
            if (hoaDonChiTiet == null) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Sản phẩm không thuộc giỏ hàng."));
            }

            int currentQuantity = hoaDonChiTiet.getSoLuong();
            int newQuantity = request.getQuantity();

            if (newQuantity < 0) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Số lượng không hợp lệ."));
            }

            // Kiểm tra nếu tăng số lượng
            if (newQuantity > currentQuantity) {
                int additionalQuantity = newQuantity - currentQuantity;

                // Kiểm tra tồn kho có đủ số lượng không
                if (sanPhamChiTiet.getSoLuong() < additionalQuantity) {
                    return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Không đủ số lượng sản phẩm trong kho."));
                }

                // Giảm số lượng tồn kho
                sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() - additionalQuantity);

            } else if (newQuantity < currentQuantity) {
                // Nếu giảm số lượng, hoàn lại số lượng vào kho
                int returnedQuantity = currentQuantity - newQuantity;
                sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() + returnedQuantity);
            }

            // Cập nhật hoặc xóa sản phẩm trong giỏ hàng
            if (newQuantity == 0) {
                hoaDonService.removeProductFromCart(request.getProductId());
            } else {
                hoaDonService.updateQuantity(request.getProductId(), newQuantity);
            }

            // Lưu thay đổi tồn kho
            sanPhamChiTietRepository.save(sanPhamChiTiet);

            return ResponseEntity.ok(Map.of("success", true, "message", "Cập nhật số lượng thành công."));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Đã xảy ra lỗi: " + e.getMessage()));
        }
    }

    @DeleteMapping("/ban-hang/{hoaDonId}/remove-product/{sanPhamChiTietId}")
    public ResponseEntity<Map<String, Object>> removeProductFromOrder(
            @PathVariable Integer hoaDonId,
            @PathVariable Integer sanPhamChiTietId) {

        Integer nhanVienId = nhanVienService.getLoggedInNhanVienId();
        Optional<NhanVien> nhanVienOptional = nhanVienRepo.findById(nhanVienId);

        if (nhanVienOptional.isPresent()) {
            // Tìm hóa đơn
            HoaDon hoaDon = hoaDonRepository.findById(hoaDonId)
                    .orElseThrow(() -> new RuntimeException("Hóa đơn không tồn tại."));

            hoaDon.setNhanVien(nhanVienOptional.get());
            hoaDonRepository.save(hoaDon);
            // Tìm sản phẩm trong hóa đơn
            List<HoaDonChiTiet> hoaDonChiTietList = hoaDon.getHoaDonChiTietList();

            HoaDonChiTiet chiTiet = hoaDonChiTietList.stream()
                    .filter(item -> item.getSanPhamChiTiet().getId().equals(sanPhamChiTietId))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại trong hóa đơn."));

            // Hoàn lại số lượng sản phẩm vào kho
            SanPhamChiTiet sanPhamChiTiet = chiTiet.getSanPhamChiTiet();
            sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() + chiTiet.getSoLuong());
            sanPhamChiTietRepository.save(sanPhamChiTiet);

            // Xóa sản phẩm khỏi hóa đơn
            hoaDonChiTietList.remove(chiTiet);
            hoaDon.setHoaDonChiTietList(hoaDonChiTietList);

            // Cập nhật tổng giá trị hóa đơn
            double newTotal = (hoaDon.getTongGia() != null ? hoaDon.getTongGia() : 0)
                    - chiTiet.getDonGia() * chiTiet.getSoLuong();
            hoaDon.setTongGia(Math.max(newTotal, 0)); // Đảm bảo tổng giá trị không âm

            // Nếu không còn sản phẩm nào, có thể cập nhật trạng thái hóa đơn
            if (hoaDonChiTietList.isEmpty()) {
                hoaDon.setTrangThai("TRỐNG");
            }

            // Lưu hóa đơn cập nhật
            hoaDonRepository.save(hoaDon);

            // Xóa sản phẩm khỏi cơ sở dữ liệu
            hoaDonChiTietRepository.delete(chiTiet);

            // Trả về kết quả
            return ResponseEntity.ok(Map.of(
                    "message", "Đã xóa sản phẩm thành công.",
                    "totalAmount", hoaDon.getTongGia()
            ));

        }
        return null;
    }

    @GetMapping("/ban-hang/{hoaDonId}/products")
    public ResponseEntity<List<Map<String, Object>>> getProductsInOrder(@PathVariable Integer hoaDonId) {
        HoaDon hoaDon = hoaDonRepository.findById(hoaDonId)
                .orElseThrow(() -> new RuntimeException("Hóa đơn không tồn tại"));

        List<HoaDonChiTiet> details = hoaDonChiTietRepository.findByHoaDon(hoaDon);

        // Sử dụng kiểu dữ liệu đơn giản cho Map
        List<Map<String, Object>> products = details.stream().map(detail -> {
            Map<String, Object> product = new HashMap<>();
            product.put("id", detail.getSanPhamChiTiet().getId());
            product.put("productName", detail.getSanPhamChiTiet().getSanPham().getTen());
            product.put("quantity", detail.getSoLuong());
            product.put("price", detail.getDonGia());
            product.put("totalPrice", detail.getDonGia() * detail.getSoLuong());
            return product;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(products);
    }

    @PostMapping("/ban-hang/{hoaDonId}/payment")
    public ResponseEntity<Map<String, Object>> processPayment(
            @PathVariable Integer hoaDonId,
            @RequestParam String paymentMethod) {

        // Kiểm tra nếu hóa đơn không tồn tại
        HoaDon hoaDon = hoaDonRepository.findById(hoaDonId)
                .orElseThrow(() -> new RuntimeException("Hóa đơn không tồn tại"));

        // Xử lý thanh toán (ở đây là thanh toán bằng tiền mặt)
        if ("cash".equals(paymentMethod)) {
            // Thực hiện xử lý thanh toán tiền mặt
            hoaDon.setTrangThai("Đã thanh toán");  // Ví dụ: Cập nhật trạng thái hóa đơn
            hoaDonRepository.save(hoaDon);

            return ResponseEntity.ok(Collections.singletonMap("message", "Thanh toán thành công"));
        } else {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Phương thức thanh toán không hợp lệ"));
        }
    }

    @PostMapping("/ban-hang/{hoaDonId}/thanh-toan")
    public ResponseEntity<Map<String, Object>> thanhToan(@PathVariable Integer hoaDonId,
                                                         @RequestParam("customerPayment") Double customerPayment) {
        try {
            // Tìm hóa đơn theo ID
            HoaDon hoaDon = hoaDonService.findByid(hoaDonId);
            if (hoaDon == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Hóa đơn không tồn tại!"));
            }

            // Kiểm tra số tiền khách đưa
            if (customerPayment < hoaDon.getTongGia()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "Số tiền khách đưa không đủ để thanh toán!"));
            }

            // Tính số tiền thừa
            Double tienThua = customerPayment - hoaDon.getTongGia();

            // Cập nhật thông tin hóa đơn
            hoaDon.setTienKhachDua(customerPayment);
            hoaDon.setTienThua(tienThua);
            hoaDon.setTrangThai("Đã thanh toán");
            hoaDonRepository.save(hoaDon);

            // Định dạng số tiền trả về
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Thanh toán thành công!");
            response.put("customerPayment", currencyFormat.format(customerPayment));
            response.put("changeAmount", currencyFormat.format(tienThua));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Đã xảy ra lỗi trong quá trình xử lý!"));
        }
    }

    @GetMapping("/ban-hang/in-hoa-don/{id}")
    public void inHoaDon(@PathVariable Integer id, HttpServletResponse response) throws IOException {
        HoaDon hoaDon = hoaDonService.findByid(id);

        // Tạo tài liệu Word
        XWPFDocument document = new XWPFDocument();

        // Tiêu đề hóa đơn (canh giữa và chữ to, chỉ làm đậm phần tiêu đề)
        XWPFParagraph titleParagraph = document.createParagraph();
        titleParagraph.setAlignment(ParagraphAlignment.CENTER); // Canh giữa
        XWPFRun titleRun = titleParagraph.createRun();
        titleRun.setText("Hóa đơn thanh toán: " + hoaDon.getId());
        titleRun.setBold(true); // Làm chữ đậm
        titleRun.setFontSize(18); // Cỡ chữ lớn hơn
        titleRun.addBreak();

        // Thông tin khách hàng
        XWPFParagraph customerParagraph = document.createParagraph();
        XWPFRun customerRun = customerParagraph.createRun();
        customerRun.setText("Tên khách hàng: " + hoaDon.getKhachHang().getTen());
        customerRun.addBreak();
        customerRun.setText("Số điện thoại: " + hoaDon.getKhachHang().getSdt());

        // Hiển thị ngày tạo bên dưới bảng
        // Hiển thị ngày tạo bên dưới bảng
        XWPFParagraph dateParagraph = document.createParagraph();
        XWPFRun dateRun = dateParagraph.createRun();
        dateRun.setText("Ngày tạo: " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(hoaDon.getNgayTao()));
        dateRun.addBreak();

        NumberFormat currencyFormat = NumberFormat.getNumberInstance(new Locale("vi", "VN"));

        // Thêm chi tiết sản phẩm dưới dạng bảng
        XWPFTable table = document.createTable();

        // Tạo header của bảng và làm đậm
        XWPFTableRow headerRow = table.getRow(0);
        XWPFRun headerRun0 = headerRow.getCell(0).getParagraphs().get(0).createRun();
        headerRun0.setText("Tên sản phẩm");
        headerRun0.setBold(true);

        XWPFRun headerRun1 = headerRow.addNewTableCell().getParagraphs().get(0).createRun();
        headerRun1.setText("Số lượng");
        headerRun1.setBold(true);

        XWPFRun headerRun2 = headerRow.addNewTableCell().getParagraphs().get(0).createRun();
        headerRun2.setText("Đơn giá");
        headerRun2.setBold(true);

        XWPFRun headerRun3 = headerRow.addNewTableCell().getParagraphs().get(0).createRun();
        headerRun3.setText("Tổng giá");
        headerRun3.setBold(true);

        // Đặt chiều rộng cố định cho các cột
        table.setWidth("100%"); // Cấu hình cho bảng rộng tối đa
        headerRow.getCell(0).setWidth("4000"); // Cột "Tên sản phẩm" rộng hơn
        headerRow.getCell(1).setWidth("1000");
        headerRow.getCell(2).setWidth("1500");
        headerRow.getCell(3).setWidth("1500");

        // Thêm các chi tiết sản phẩm vào bảng
        for (HoaDonChiTiet chiTiet : hoaDon.getHoaDonChiTietList()) {
            XWPFTableRow row = table.createRow();
            row.getCell(0).setText(chiTiet.getSanPhamChiTiet().getSanPham().getTen());
            row.getCell(1).setText(String.valueOf(chiTiet.getSoLuong()));
            row.getCell(2).setText(currencyFormat.format(chiTiet.getDonGia()) + " VNĐ");
            row.getCell(3).setText(currencyFormat.format(chiTiet.getDonGia() * chiTiet.getSoLuong()) + " VNĐ");
        }

        // Tổng tiền
        XWPFParagraph totalParagraph = document.createParagraph();
        XWPFRun totalRun = totalParagraph.createRun();
        totalRun.addBreak();
        totalRun.setText("Tổng tiền: " + currencyFormat.format(hoaDon.getTongGia()) + " VNĐ");
        totalRun.addBreak();

        // Số tiền khách đưa
        totalRun.setText("Tiền khách đưa: " + currencyFormat.format(hoaDon.getTienKhachDua()) + " VNĐ");
        totalRun.addBreak();

        // Số tiền thừa
        totalRun.setText("Tiền thừa: " + currencyFormat.format(hoaDon.getTienThua()) + " VNĐ");

        // Cấu hình để tải xuống file
        response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        response.setHeader("Content-Disposition", "attachment; filename=hoa-don-" + hoaDon.getId() + ".docx");

        // Xuất file Word vào response
        document.write(response.getOutputStream());
        document.close();
    }

}

