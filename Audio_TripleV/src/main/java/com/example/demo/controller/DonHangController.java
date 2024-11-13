package com.example.demo.controller;

import com.example.demo.entity.DonHang;
import com.example.demo.entity.DonHangChiTiet;
import com.example.demo.entity.Hang;
import com.example.demo.entity.LoaiSanPham;
import com.example.demo.entity.MauSac;
import com.example.demo.entity.SanPham;
import com.example.demo.entity.SanPhamChiTiet;
import com.example.demo.repository.DonHangChiTietRepository;
import com.example.demo.repository.DonHangRepository;
import com.example.demo.repository.HangRepository;
import com.example.demo.repository.LoaiSanPhamRepository;
import com.example.demo.repository.MauSacRepository;
import com.example.demo.repository.SanPhamChiTietRepository;
import com.example.demo.repository.SanPhamRepository;
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
import org.springframework.web.bind.annotation.RequestParam;

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

    @Autowired
    private LoaiSanPhamRepository loaiSanPhamRepository;

    @Autowired
    private SanPhamRepository sanPhamRepository;

    @Autowired
    private MauSacRepository mauSacRepository;

    @Autowired
    private HangRepository hangRepository;

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
    public String donHang(@RequestParam(required = false) Integer idLoaiSP,
                          @RequestParam(required = false) String sanPhamTen,
                          @RequestParam(required = false) Integer mauSac,
                          @RequestParam(required = false) Integer hang,
                          @RequestParam(required = false) Double minPrice,
                          @RequestParam(required = false) Double maxPrice,
                          @RequestParam(required = false) String donGia, // Thêm tham số donGia
                          @PathVariable Integer id, Model model) {
        DonHang dh = donHangService.findByid(id);
        model.addAttribute("donHang", dh);
        model.addAttribute("donHangChiTiet", new DonHangChiTiet());

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
                (sanPhamTen == null || sanPhamTen.isEmpty()) && // Sửa từ idSanPham thành sanPhamTen
                (mauSac == null || mauSac == 0) &&
                (hang == null || hang == 0) &&
                (minPrice == null || maxPrice == null)) {
            // Nếu không chọn bộ lọc nào thì lấy tất cả sản phẩm
            list = sanPhamChiTietRepository.findAll();
        } else {
            // Lấy danh sách sản phẩm theo các bộ lọc
            list = sanPhamChiTietRepository.findByFilters(
                    idLoaiSP, sanPhamTen, mauSac, hang, minPrice, maxPrice); // Truyền sanPhamTen vào findByFilters
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

    @PostMapping("ban-hang/save")
    public String saveDonHang(@ModelAttribute("donHang") DonHang donHang,
                              DonHangChiTiet donHangChiTiet) {

        if (donHang.getTrangThai() == null || donHang.getTrangThai().isEmpty()) {
            donHang.setTrangThai("Chưa thanh toán"); // Thiết lập mặc định nếu trạng thái chưa có
        }
        if (donHang.getTongGia() == null) {
            donHang.setTongGia(0.0); // Thiết lập tổng giá mặc định
        }
        donHang.setNgayTao(new Date());
        donHang.setNgayCapNhat(new Date());

        donHangRepository.save(donHang);
        return "redirect:/user/don-hang";
    }


    //    @GetMapping("ban-hang/chi-tiet/{id}")
//    public String donHangChiTiet(@PathVariable Integer id, Model model) {
//        return "nhanvien/donhang";
//    }


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
                                   @RequestParam("spctIds") List<Integer> spctIds,
                                   @RequestParam("soLuong") List<Integer> quantities,
                                   Model model) {
        DonHang donHang = donHangService.findByid(id);
        if (donHang == null) {
            model.addAttribute("error", "Đơn hàng không tồn tại!");
            return "error";
        }

        double totalAmount = 0;

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
            DonHangChiTiet donHangChiTiet = new DonHangChiTiet();
            donHangChiTiet.setDonHang(donHang);
            donHangChiTiet.setSanPhamChiTiet(sanPhamChiTiet);
            donHangChiTiet.setDonGia(sanPhamChiTiet.getDonGia());
            donHangChiTiet.setSoLuong(soLuong);
            donHangChiTiet.setNgayTao(new Date());
            donHangChiTiet.setNgayCapNhat(new Date());

            // Trừ số lượng từ SanPhamChiTiet và lưu lại
            sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() - soLuong);
            sanPhamChiTietRepository.save(sanPhamChiTiet); // Lưu số lượng cập nhật vào CSDL

            // Tính tổng cho đơn hàng
            double lineTotal = sanPhamChiTiet.getDonGia() * soLuong;
            totalAmount += lineTotal;

            // Lưu chi tiết đơn hàng
            donHangChiTietRepository.save(donHangChiTiet);
        }

        // Thiết lập và lưu tổng giá cho DonHang
        donHang.setTongGia(totalAmount);
        donHangRepository.save(donHang);

        return "redirect:/user/don-hang";
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
