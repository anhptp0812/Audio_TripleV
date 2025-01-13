package com.example.demo.api;


import com.example.demo.entity.CapNhatSoLuongRequest;
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
import com.example.demo.repository.VoucherRepository;
import com.example.demo.service.DonHangService;
import com.example.demo.service.HoaDonService;
import com.example.demo.service.NhanVienService;
import com.example.demo.service.SanPhamChiTietService;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import jakarta.servlet.http.HttpServletResponse;
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
import java.io.OutputStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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

            if (hoaDon.getSoTienPhaiTra() != null) {
                hoaDon.setFormattedSoTienPhaiTra(currencyFormat.format(hoaDon.getSoTienPhaiTra()));
            } else {
                hoaDon.setFormattedSoTienPhaiTra(currencyFormat.format(0.0)); // Thay thế với giá trị mặc định nếu null
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
            System.out.println("Hoa Don Found: " + hoaDon.getId());

            // Định dạng giá trị tiền tệ
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

            // Định dạng các giá trị trong hoaDon
            hoaDon.setFormattedTongGia(currencyFormat.format(hoaDon.getTongGia()));
            hoaDon.setFormattedTienKhachDua(currencyFormat.format(hoaDon.getTienKhachDua()));
            hoaDon.setFormattedTienThua(currencyFormat.format(hoaDon.getTienThua()));

            // Tính toán số tiền phải trả với voucher
            if (hoaDon.getVouCher() != null) {
                double voucherAmount = 0.0;
                // Kiểm tra nếu voucher là giá trị tiền hoặc phần trăm
                if (hoaDon.getVouCher().getLoai().equals("GiamTien") && hoaDon.getVouCher().getGiaTriTien() != null) {
                    voucherAmount = hoaDon.getVouCher().getGiaTriTien(); // Giá trị tiền
                    hoaDon.getVouCher().setFormattedGiaTriTien(currencyFormat.format(voucherAmount));
                } else if (hoaDon.getVouCher().getLoai().equals("GiamPhanTram") && hoaDon.getVouCher().getGiaTriPhanTram() != null) {
                    voucherAmount = hoaDon.getTongGia() * hoaDon.getVouCher().getGiaTriPhanTram() / 100; // Giá trị phần trăm
                    hoaDon.getVouCher().setFormattedGiaTriPhanTram(String.format("%d%%", hoaDon.getVouCher().getGiaTriPhanTram().intValue()));
                }
                hoaDon.setSoTienPhaiTra(hoaDon.getTongGia() - voucherAmount);
            } else {
                hoaDon.setSoTienPhaiTra(hoaDon.getTongGia());
            }

            hoaDon.setFormattedSoTienPhaiTra(currencyFormat.format(hoaDon.getSoTienPhaiTra()));

            // Định dạng giá trị cho từng chi tiết hóa đơn
            for (HoaDonChiTiet hdct : hoaDon.getHoaDonChiTietList()) {
                // Lấy ngày tạo hóa đơn từ hoaDon (giả sử ngàyTao là java.sql.Timestamp)
//                java.sql.Timestamp ngayTaoTimestamp = hoaDon.getNgayTao();  // Nếu là java.sql.Timestamp
//                LocalDate ngayTao = ngayTaoTimestamp.toLocalDate();  // Chuyển từ Timestamp thành LocalDate (chỉ lấy ngày)

              //   Nếu bạn sử dụng java.util.Date thay cho Timestamp:
                 java.util.Date ngayTaoDate = hoaDon.getNgayTao();
                 LocalDate ngayTao = ngayTaoDate.toInstant()
                     .atZone(ZoneId.systemDefault())
                     .toLocalDate();

                // Lấy thời gian bảo hành từ chi tiết sản phẩm
                int thoiGianBaoHanh = hdct.getSanPhamChiTiet().getThoiGianBaoHanh();  // Thời gian bảo hành (số tháng)

                // Tính toán ngày kết thúc bảo hành (ngày tạo + số tháng bảo hành)
                LocalDate ngayKetThucBaoHanh = ngayTao.plusMonths(thoiGianBaoHanh);

                // Thêm ngày kết thúc bảo hành vào chi tiết hóa đơn
                hdct.setThoiGianKetThucBH(ngayKetThucBaoHanh);  // Lưu ngày kết thúc bảo hành vào chi tiết hóa đơn

                // Định dạng ngày kết thúc bảo hành (để hiển thị)
                String formattedNgayKetThucBaoHanh = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(ngayKetThucBaoHanh);
                hdct.setFormattedNgayKetThucBaoHanh(formattedNgayKetThucBaoHanh);  // Lưu định dạng ngày kết thúc bảo hành

                // Định dạng giá trị cho từng chi tiết hóa đơn
                hdct.setFormattedDonGia(currencyFormat.format(hdct.getDonGia()));  // Định dạng giá đơn
                hdct.setFormattedTongGia(currencyFormat.format(hdct.getTongGia()));  // Định dạng tổng giá
            }

            model.addAttribute("hoaDon", hoaDon);
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
                if(hoaDon.getVouCher() != null){
                    hoaDon.setVouCher(null);
                }

                // Giảm số lượng trong kho
                sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() - soLuong);
                sanPhamChiTietRepository.save(sanPhamChiTiet);

                // Cập nhật tổng giá hóa đơn
                double newTotal = (hoaDon.getTongGia() != null ? hoaDon.getTongGia() : 0)
                        + sanPhamChiTiet.getDonGia() * soLuong;
                hoaDon.setTongGia(newTotal);
                hoaDon.setSoTienPhaiTra(newTotal);
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

            // Nếu không còn sản phẩm nào, có thể cập nhật trạng thái hóa đơn và xóa voucher
            if (hoaDonChiTietList.isEmpty()) {
                hoaDon.setVouCher(null);  // Xóa voucher nếu không còn sản phẩm nào
            }

            hoaDon.setVouCher(null);

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
            if (customerPayment < hoaDon.getSoTienPhaiTra()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "Số tiền khách đưa không đủ để thanh toán!"));
            }

            // Định dạng số tiền trả về
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

            Double tienThua;

            if (hoaDon.getVouCher() != null) {
                // Tính số tiền thừa
                tienThua = customerPayment - hoaDon.getSoTienPhaiTra();
            } else {
                tienThua = customerPayment - hoaDon.getTongGia();
                hoaDon.setSoTienPhaiTra(hoaDon.getTongGia());
            }
            // Cập nhật thông tin hóa đơn
            hoaDon.setNgayCapNhat(new Date());
            hoaDon.setTienKhachDua(customerPayment);
            hoaDon.setTienThua(tienThua);
            hoaDon.setTrangThai("Đã thanh toán");
            hoaDonRepository.save(hoaDon);

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
        if (hoaDon == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy hóa đơn");
            return;
        }

        // Cấu hình xuất file PDF
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=hoa-don-" + hoaDon.getId() + ".pdf");

        try (OutputStream out = response.getOutputStream()) {
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Cài đặt font hỗ trợ tiếng Việt (ví dụ font Arial Unicode MS)
            PdfFont font = PdfFontFactory.createFont("c:/windows/fonts/arial.ttf", PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
            document.setFont(font);

            // Tiêu đề hóa đơn
            Paragraph title = new Paragraph("HÓA ĐƠN THANH TOÁN")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBold()
                    .setFontSize(18);
            document.add(title);

            // Thông tin khách hàng
            document.add(new Paragraph("Mã hóa đơn: " + hoaDon.getId()));
            document.add(new Paragraph("Tên khách hàng: " + hoaDon.getKhachHang().getTen()));
            document.add(new Paragraph("Số điện thoại: " + hoaDon.getKhachHang().getSdt()));
            document.add(new Paragraph("Ngày tạo: " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(hoaDon.getNgayTao())));
            document.add(new Paragraph("\n"));

            // Bảng chi tiết sản phẩm
            Table table = new Table(new float[]{4, 2, 2, 2, 3, 3});
            table.setWidth(UnitValue.createPercentValue(100));

            // Header của bảng
            table.addHeaderCell(new Cell().add(new Paragraph("Tên sản phẩm").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Số lượng").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Đơn giá").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Tổng giá").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Thời gian bảo hành").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Ngày kết thúc BH").setBold()));

            // Thêm các chi tiết sản phẩm
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            // Thêm các chi tiết sản phẩm
            for (HoaDonChiTiet chiTiet : hoaDon.getHoaDonChiTietList()) {
                table.addCell(chiTiet.getSanPhamChiTiet().getSanPham().getTen());
                table.addCell(String.valueOf(chiTiet.getSoLuong()));
                table.addCell(currencyFormat.format(chiTiet.getDonGia()));
                table.addCell(currencyFormat.format(chiTiet.getTongGia()));

                // Thời gian bảo hành
                table.addCell(String.valueOf(chiTiet.getSanPhamChiTiet().getThoiGianBaoHanh()) + " tháng");

                // Ngày kết thúc bảo hành
                LocalDate ngayKetThuc = chiTiet.getSanPhamChiTiet().calculateNgayKetThucBaoHanh();
                String ngayKetThucStr = ngayKetThuc != null
                        ? ngayKetThuc.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                        : "Không xác định";
                table.addCell(ngayKetThucStr);
            }

            document.add(table);

            // Tổng tiền, tiền phải trả, tiền khách đưa, tiền thừa
            document.add(new Paragraph("\nTổng tiền: " + currencyFormat.format(hoaDon.getTongGia())));
            // Thông tin voucher
            if (hoaDon.getVouCher() != null) {
                document.add(new Paragraph("Voucher: " + hoaDon.getVouCher().getTen()));
                if ("GiamTien".equalsIgnoreCase(hoaDon.getVouCher().getLoai())) {
                    document.add(new Paragraph("Giảm tiền: " + currencyFormat.format(hoaDon.getVouCher().getGiaTriTien())));
                } else if ("GiamPhanTram".equalsIgnoreCase(hoaDon.getVouCher().getLoai())) {
                    document.add(new Paragraph("Giảm phần trăm: " + String.format("%d%%", hoaDon.getVouCher().getGiaTriPhanTram().intValue())));
                }
            }
            document.add(new Paragraph("Tiền phải trả: " + currencyFormat.format(hoaDon.getSoTienPhaiTra())));
            document.add(new Paragraph("Tiền khách đưa: " + currencyFormat.format(hoaDon.getTienKhachDua())));
            document.add(new Paragraph("Tiền thừa: " + currencyFormat.format(hoaDon.getTienThua())));

            document.close();
        }
    }

}

