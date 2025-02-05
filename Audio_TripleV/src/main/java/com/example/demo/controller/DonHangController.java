package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.repository.DonHangRepository;
import com.example.demo.repository.SanPhamChiTietRepository;
import com.example.demo.service.DonHangService;
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

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@CrossOrigin(origins = "http://localhost:3000") // Thay đổi URL này theo miền của frontend
@Controller
@RequestMapping("/user")
public class DonHangController {
    //    @Autowired
//    private VNPayConfig vnpayConfig;

    @Autowired
    private DonHangService donHangService;

    @Autowired
    private DonHangRepository donHangRepository;

    @GetMapping("/don-hang")
    public String index(@RequestParam(value = "trangThai", required = false) String trangThai, Model model) {
        List<DonHang> list;

        // Lọc đơn hàng theo trạng thái nếu có
        if (trangThai != null && !trangThai.isEmpty()) {
            list = donHangRepository.findByTrangThai(trangThai);
        } else {
            list = donHangRepository.findAll();
        }

        // Định dạng giá tiền thành VND
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        list.forEach(dh -> {
            String formattedPrice = currencyFormat.format(dh.getTongGia());
            dh.setFormattedTongGia(formattedPrice); // Đặt thêm thuộc tính để lưu giá định dạng
        });

        // Thông báo nếu không có đơn hàng phù hợp
        if (list.isEmpty()) {
            model.addAttribute("message", "Không có đơn hàng phù hợp với tiêu chí tìm kiếm.");
        }
        model.addAttribute("listDH", list);

        // Lựa chọn trạng thái có sẵn
        List<String> trangThaiOptions = List.of("Chờ xử lý", "Đã xác nhận", "Đang giao hàng", "Đã giao hàng", "Đã hủy");
        model.addAttribute("trangThaiOptions", trangThaiOptions);

        // Thêm đối tượng DonHang vào model
        model.addAttribute("dh", new DonHang()); // Thêm đối tượng DonHang rỗng vào model

        return "nhanvien/donhang"; // Trả về file HTML
    }

    @GetMapping("/don-hang/detail/{id}")
    public String viewOrderDetail(@PathVariable Integer id, Model model) {
        DonHang donHang = donHangService.findByid(id);
        if (donHang == null) {
            model.addAttribute("message", "Không tìm thấy đơn hàng với ID: " + id);
            return "error/404";
        }

        // Định dạng tổng giá và các chi tiết đơn hàng
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        // Định dạng tổng giá
        String formattedTongGia = currencyFormat.format(donHang.getTongGia());
        donHang.setFormattedTongGia(formattedTongGia);

        // Định dạng các chi tiết đơn hàng
        donHang.getDonHangChiTietList().forEach(chiTiet -> {
            // Định dạng đơn giá
            String formattedDonGia = currencyFormat.format(chiTiet.getDonGia());
            chiTiet.setFormattedDonGia(formattedDonGia);

            // Tính và định dạng thành tiền
            double thanhTien = chiTiet.getDonGia() * chiTiet.getSoLuong();
            String formattedThanhTien = currencyFormat.format(thanhTien);
            chiTiet.setFormattedThanhTien(formattedThanhTien);
        });
// Định dạng giá trị cho từng chi tiết hóa đơn
        for (DonHangChiTiet donHangChiTiet : donHang.getDonHangChiTietList()) {
            // Lấy ngày tạo hóa đơn từ hoaDon (giả sử ngàyTao là java.sql.Timestamp)
//                java.sql.Timestamp ngayTaoTimestamp = hoaDon.getNgayTao();  // Nếu là java.sql.Timestamp
//                LocalDate ngayTao = ngayTaoTimestamp.toLocalDate();  // Chuyển từ Timestamp thành LocalDate (chỉ lấy ngày)

            //   Nếu bạn sử dụng java.util.Date thay cho Timestamp:
            java.util.Date ngayTaoDate = donHangChiTiet.getNgayTao();
            LocalDate ngayTao = ngayTaoDate.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            // Lấy thời gian bảo hành từ chi tiết sản phẩm
            int thoiGianBaoHanh = donHangChiTiet.getSanPhamChiTiet().getThoiGianBaoHanh();  // Thời gian bảo hành (số tháng)

            // Tính toán ngày kết thúc bảo hành (ngày tạo + số tháng bảo hành)
            LocalDate ngayKetThucBaoHanh = ngayTao.plusMonths(thoiGianBaoHanh);

            // Thêm ngày kết thúc bảo hành vào chi tiết hóa đơn
            donHangChiTiet.setThoiGianKetThucBH(ngayKetThucBaoHanh);  // Lưu ngày kết thúc bảo hành vào chi tiết hóa đơn

            // Định dạng ngày kết thúc bảo hành (để hiển thị)
            String formattedNgayKetThucBaoHanh = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(ngayKetThucBaoHanh);
            donHangChiTiet.setFormattedNgayKetThucBaoHanh(formattedNgayKetThucBaoHanh);  // Lưu định dạng ngày kết thúc bảo hành

        }
        model.addAttribute("donHang", donHang);
        return "nhanvien/detail-donhang";
    }

    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;

    @PostMapping("/don-hang/update-status")
    public String updateOrderStatus(@RequestParam Integer id, @RequestParam String status, RedirectAttributes redirectAttributes) {
        DonHang donHang = donHangService.findByid(id);
        if (donHang != null) {
            // Cập nhật trạng thái
            donHang.setTrangThai(status);

            // Cập nhật ngày cập nhật trạng thái
            donHang.setNgayCapNhat(new Date());
            if ("Đã hủy".equals(status)) {
                for (DonHangChiTiet chiTiet : donHang.getDonHangChiTietList()) {
                    SanPhamChiTiet sanPhamChiTiet = chiTiet.getSanPhamChiTiet();
                    sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() + chiTiet.getSoLuong());
                    sanPhamChiTietRepository.save(sanPhamChiTiet);
                }
            }

            if ("Chờ xử lý".equals(status)) {
                for (DonHangChiTiet chiTiet : donHang.getDonHangChiTietList()) {
                    SanPhamChiTiet sanPhamChiTiet = chiTiet.getSanPhamChiTiet();

                    // Kiểm tra số lượng trong kho có đủ để giảm không
                    if (sanPhamChiTiet.getSoLuong() >= chiTiet.getSoLuong()) {
                        sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() - chiTiet.getSoLuong());
                        sanPhamChiTietRepository.save(sanPhamChiTiet);
                    } else {
                        // Nếu không đủ số lượng trong kho, thêm thông báo lỗi
                        redirectAttributes.addFlashAttribute("errorMessage", "Không đủ sản phẩm trong kho để chuyển về trạng thái 'Chờ xử lý'!");
                        return "redirect:/user/don-hang/detail/" + id;
                    }
                }
            }

            // Nếu trạng thái là 'Đã giao hàng', cập nhật thêm ngày giao
            if ("Đã giao hàng".equals(status)) {
                donHang.setNgayGiao(new Date());
                donHang.setTrangThaiPayment("Đã thanh toán");
            }
            // Lưu lại thông tin thay đổi
            donHangService.save(donHang);

            // Thêm thông báo
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật trạng thái thành công!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Đơn hàng không tồn tại!");
        }
        return "redirect:/user/don-hang/detail/" + id;
    }

    @GetMapping("/don-hang/in-don-hang/{id}")
    public void inDonHang(@PathVariable Integer id, HttpServletResponse response) throws IOException {
        DonHang donHang = donHangService.findByid(id);
        if (donHang == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy đơn hàng");
            return;
        }

        // Cấu hình xuất file PDF
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=don-hang-" + donHang.getId() + ".pdf");

        try (OutputStream out = response.getOutputStream()) {
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Cài đặt font hỗ trợ tiếng Việt (ví dụ font Arial Unicode MS)
            PdfFont font = PdfFontFactory.createFont("c:/windows/fonts/arial.ttf", PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
            document.setFont(font);

            // Tiêu đề hóa đơn
            Paragraph title = new Paragraph("Đơn Hàng")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBold()
                    .setFontSize(18);
            document.add(title);

            // Thông tin khách hàng
            document.add(new Paragraph("Mã đơn hàng: " + donHang.getId()));
            document.add(new Paragraph("Tên khách hàng: " + donHang.getKhachHang().getTen()));
            document.add(new Paragraph("Số điện thoại: " + donHang.getKhachHang().getSdt()));
            document.add(new Paragraph("Ngày tạo: " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(donHang.getNgayTao())));
            document.add(new Paragraph("Địa Chỉ: " + donHang.getKhachHang().getDiaChi()));
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
            for (DonHangChiTiet chiTiet : donHang.getDonHangChiTietList()) {
                table.addCell(chiTiet.getSanPhamChiTiet().getSanPham().getTen());
                table.addCell(String.valueOf(chiTiet.getSoLuong()));
                table.addCell(currencyFormat.format(chiTiet.getDonGia()));
                table.addCell(currencyFormat.format(chiTiet.getDonGia() * chiTiet.getSoLuong()));
                table.addCell(String.valueOf(chiTiet.getSanPhamChiTiet().getThoiGianBaoHanh()) + " tháng");

                // Ngày kết thúc bảo hành
                LocalDate ngayKetThuc = chiTiet.getSanPhamChiTiet().calculateNgayKetThucBaoHanh();
                String ngayKetThucStr = ngayKetThuc != null
                        ? ngayKetThuc.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                        : "Không xác định";
                table.addCell(ngayKetThucStr);
            }
            document.add(table);

            // Tổng tiền
            if ("Đã thanh toán".equals(donHang.getTrangThaiPayment())) {
                document.add(new Paragraph("\nTổng tiền: 0 đ"));
            } else {
                document.add(new Paragraph("\nTổng tiền: " + currencyFormat.format(donHang.getTongGia())));
            }

            document.close();
        }
    }

}