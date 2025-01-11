package com.example.demo.api;

import com.example.demo.entity.DonHang;
import com.example.demo.entity.DonHangChiTiet;
import com.example.demo.entity.Hang;
import com.example.demo.entity.KhachHang;
import com.example.demo.entity.LoaiSanPham;
import com.example.demo.entity.MauSac;
import com.example.demo.entity.SanPham;
import com.example.demo.entity.SanPhamChiTiet;
import com.example.demo.repository.*;

import com.example.demo.entity.*;
import com.example.demo.repository.HangRepository;
import com.example.demo.repository.HoaDonRepository;
import com.example.demo.repository.KhachHangRepository;
import com.example.demo.repository.LoaiSanPhamRepository;
import com.example.demo.repository.MauSacRepository;
import com.example.demo.repository.SanPhamChiTietRepository;
import com.example.demo.repository.SanPhamRepository;
import com.example.demo.service.ArticleService;
import com.example.demo.service.CommentService;
import com.example.demo.service.HoaDonService;

import com.example.demo.service.KhachHangService;
import com.example.demo.service.NhanVienService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
//@RestController
public class NhanVienController {

    @Autowired
    private LoaiSanPhamRepository loaiSanPhamRepository;

    @Autowired
    private SanPhamRepository sanPhamRepository;

    @Autowired
    private MauSacRepository mauSacRepository;

    @Autowired
    private HangRepository hangRepository;

    @Autowired
    private HinhAnhRepository hinhAnhRepository;

    @Autowired
    private NhanVienService nhanVienService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/user/trang-chu")
    public String index() {
        return "nhanvien/tin_tuc";
    }

    @GetMapping("/user/thong-tin")
    public String thongTinTaiKhoanNV(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        // Lấy khách hàng hiện tại
        NhanVien nhanVien = nhanVienService.findByTaiKhoan(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Nhân Viên không tồn tại"));
        model.addAttribute("nhanVien", nhanVien);
        return "nhanvien/thong-tin"; // Trang hiển thị danh sách đơn hàng
    }

    @PostMapping("/user/thong-tin/doi-mat-khau")
    public String doiMatKhau(@AuthenticationPrincipal UserDetails userDetails,
                             @RequestParam(value = "matKhauCu", required = false) String matKhauCu,
                             @RequestParam(value = "matKhauMoi", required = false) String matKhauMoi,
                             @RequestParam(value = "xacNhanMatKhauMoi", required = false) String xacNhanMatKhauMoi,
                             RedirectAttributes redirectAttributes) {
        if (matKhauCu == null || matKhauMoi == null || xacNhanMatKhauMoi == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng điền đầy đủ thông tin!");
            return "redirect:/user/thong-tin";
        }

        // Lấy thông tin nhân viên
        NhanVien nhanVien = nhanVienService.findByTaiKhoan(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Nhân Viên không tồn tại"));

        // Kiểm tra mật khẩu cũ
        if (!passwordEncoder.matches(matKhauCu, nhanVien.getPassword())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Mật khẩu cũ không đúng!");
            return "redirect:/user/thong-tin";
        }

        // Kiểm tra mật khẩu mới và xác nhận mật khẩu
        if (!matKhauMoi.equals(xacNhanMatKhauMoi)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Mật khẩu mới và xác nhận mật khẩu không khớp!");
            return "redirect:/user/thong-tin";
        }

        // Đổi mật khẩu
        nhanVien.setPassword(passwordEncoder.encode(matKhauMoi));
        nhanVienService.save(nhanVien);

        redirectAttributes.addFlashAttribute("successMessage", "Đổi mật khẩu thành công!");
        return "redirect:/user/thong-tin";
    }

    @GetMapping("/user/thong-tin/update")
    public String editThongTinTaiKhoan(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        // Lấy thông tin nhân viên hiện tại
        NhanVien nhanVien = nhanVienService.findByTaiKhoan(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Nhân viên không tồn tại"));
        model.addAttribute("nhanVien", nhanVien);
        return "nhanvien/thong-tin-update"; // Trang chỉnh sửa thông tin
    }

    @PostMapping("/user/thong-tin/update")
    public String updateThongTinTaiKhoan(@AuthenticationPrincipal UserDetails userDetails, @ModelAttribute NhanVien nhanVien) {
        // Cập nhật thông tin nhân viên
        NhanVien existingNhanVien = nhanVienService.findByTaiKhoan(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Nhân viên không tồn tại"));

        // Cập nhật các thuộc tính
        existingNhanVien.setTen(nhanVien.getTen());
        existingNhanVien.setNgaySinh(nhanVien.getNgaySinh());
        existingNhanVien.setGioiTinh(nhanVien.getGioiTinh());
        existingNhanVien.setDiaChi(nhanVien.getDiaChi());
        existingNhanVien.setEmail(nhanVien.getEmail());
        existingNhanVien.setSdt(nhanVien.getSdt());

        nhanVienService.save(existingNhanVien); // Lưu thông tin cập nhật
        return "redirect:/user/thong-tin"; // Quay lại trang thông tin tài khoản
    }

    @Autowired
    private KhachHangService khachHangService;

    @GetMapping("/user/khach-hang")
    public String hienThiKhachHang(Model model) {
        List<KhachHang> danhSachKhachHang = khachHangService.layTatCaKhachHang();
        model.addAttribute("danhSachKhachHang", danhSachKhachHang);
        return "nhanvien/khach-hang";
    }

    // Trang thêm mới khách hàng
    @GetMapping("/user/khach-hang/add")
    public String themKhachHang(Model model) {
        // Lấy danh sách các vai trò từ dịch vụ
        model.addAttribute("khachHang", new KhachHang()); // Thêm đối tượng KhachHang rỗng vào model
        return "nhanvien/khach-hang-add"; // Trang HTML để thêm khách hàng
    }

    // Xử lý thêm mới khách hàng
    @PostMapping("/user/khach-hang/add")
    public String themKhachHang(@ModelAttribute KhachHang khachHang) {
        // Lưu khách hàng vào cơ sở dữ liệu
        String encodedPassword = passwordEncoder.encode(khachHang.getMatKhau());
        khachHang.setMatKhau(encodedPassword);

        khachHang.setRole("KHACHHANG");
        khachHangService.themKhachHang(khachHang);

        // Quay lại trang danh sách khách hàng sau khi thêm mới thành công
        return "redirect:/user/khach-hang";
    }

    @GetMapping("/user/khach-hang/sua/{id}")
    public String suaKhachHang(@PathVariable Integer id, Model model) {
        KhachHang khachHang = khachHangService.layKhachHangTheoId(id);
        if (khachHang == null) {
            return "redirect:/user/khach-hang";
        }

        // Lấy danh sách các vai trò từ cơ sở dữ liệu
        model.addAttribute("khachHang", khachHang);

        return "nhanvien/khach-hang-update";
    }
    // Xử lý sửa khách hàng
    @PostMapping("/user/khach-hang/sua/{id}")
    public String suaKhachHang(@PathVariable Integer id, @ModelAttribute KhachHang khachHang) {
        // Lấy ngày hiện tại để cập nhật ngày đăng ký
        if (khachHang.getMatKhau() != null && !khachHang.getMatKhau().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(khachHang.getMatKhau());
            khachHang.setMatKhau(encodedPassword); // Cập nhật mật khẩu đã mã hóa
        }

        khachHang.setNgayDangKy(new Date()); // Hoặc LocalDateTime.now() nếu dùng LocalDateTime

        khachHang.setId(id); // Đảm bảo rằng id không thay đổi khi sửa
        khachHang.setRole("KHACHHANG");
        khachHangService.suaKhachHang(khachHang); // Lưu thông tin khách hàng vào cơ sở dữ liệu
        return "redirect:/user/khach-hang"; // Quay lại trang danh sách sau khi sửa khách hàng
    }

    @GetMapping("/admin/nhan-vien")
    public String listNhanVien(Model model) {
        List<NhanVien> nhanViens = nhanVienService.getAllNhanViens(); // Lấy danh sách nhân viên từ service
        model.addAttribute("nhanViens", nhanViens); // Truyền danh sách nhân viên vào model
        return "admin/nhan-vien"; // Chuyển tới trang hiển thị danh sách
    }
    @GetMapping("/admin/nhan-vien/save")
    public String showAddForm(Model model) {
        List<String> roles = nhanVienService.getAllRoles();
        model.addAttribute("roles", roles);
        model.addAttribute("nhanVien", new NhanVien()); // Tạo đối tượng nhân viên mới để hiển thị trên form
        return "admin/nhan-vien-add"; // Trả về view để hiển thị form
    }

    // Xử lý Thêm Mới Nhân Viên
    @PostMapping("/admin/nhan-vien/save")
    public String saveNhanVien(@ModelAttribute NhanVien nhanVien) {
        // Mã hóa mật khẩu trước khi lưu
        String encodedPassword = passwordEncoder.encode(nhanVien.getPassword());
        nhanVien.setPassword(encodedPassword);

        nhanVienService.saveNhanVien(nhanVien); // Lưu nhân viên vào cơ sở dữ liệu
        return "redirect:/admin/nhan-vien"; // Sau khi lưu, chuyển hướng về trang danh sách nhân viên
    }

    // Phương thức GET để hiển thị form cập nhật thông tin nhân viên
    @GetMapping("/admin/nhan-vien/sua/{id}")
    public String suaNhanVien(@PathVariable Integer id, Model model) {
        NhanVien nhanVien = nhanVienService.layNhanVienTheoId(id); // Lấy thông tin nhân viên theo id
        if (nhanVien == null) {
            // Nếu không tìm thấy nhân viên, quay lại trang danh sách nhân viên
            return "redirect:/admin/nhan-vien";
        }
        List<String> roles = nhanVienService.getAllRoles();
        model.addAttribute("roles", roles);
        model.addAttribute("nhanVien", nhanVien); // Đưa thông tin nhân viên vào model
        return "admin/nhan-vien-update"; // Trả về trang sửa nhân viên
    }

    // Phương thức POST để xử lý cập nhật thông tin nhân viên
    @PostMapping("/admin/nhan-vien/sua")
    public String suaNhanVien(@ModelAttribute NhanVien nhanVien) {
        // Kiểm tra nếu người dùng nhập mật khẩu mới, thì mã hóa mật khẩu mới
        if (nhanVien.getPassword() != null && !nhanVien.getPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(nhanVien.getPassword());
            nhanVien.setPassword(encodedPassword); // Cập nhật mật khẩu đã mã hóa
        }

        nhanVienService.suaNhanVien(nhanVien); // Gọi service để lưu thay đổi vào cơ sở dữ liệu
        return "redirect:/admin/nhan-vien"; // Quay lại trang danh sách nhân viên sau khi sửa
    }

    // Xử lý xóa nhân viên
    @GetMapping("/admin/nhan-vien/xoa/{id}")
    public String xoaNhanVien(@PathVariable("id") Integer id) {
        // Gọi service để xóa nhân viên theo id
        nhanVienService.xoaNhanVien(id);
        // Quay lại danh sách nhân viên
        return "redirect:/admin/nhan-vien"; // Điều hướng về trang danh sách nhân viên
    }


    @Autowired
    private ArticleService articleService;

    // Hiển thị danh sách bài viết
    @GetMapping("/user/bai-viet")
    public String listArticles(Model model) {
        model.addAttribute("articles", articleService.getAllArticles());
        return "nhanvien/article-list";
    }

    // Hiển thị form thêm bài viết
    @GetMapping("/user/bai-viet/add")
    public String showAddForm1(Model model) {
        model.addAttribute("article", new Article());
        return "nhanvien/article-form";
    }

    @Value("${upload.dir}")
    private String uploadDir;

    // Xử lý thêm bài viết
    @PostMapping("/user/bai-viet/add")
    public String addArticle(@ModelAttribute("article") Article article, @RequestParam("imageFile") MultipartFile imageFile) {
        try {
            // Xử lý file hình ảnh
            String imageFileName = saveImage(imageFile);

            // Gắn tên hình ảnh vào đối tượng bài viết
            article.setImageUrl(imageFileName);
            article.setCreatedAt(new Date());
            articleService.saveArticle(article);
            return "redirect:/user/bai-viet";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/user/bai-viet/add?error"; // Hiển thị lỗi nếu có
        }
    }

    // Hiển thị form sửa bài viết
    @GetMapping("/user/bai-viet/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        model.addAttribute("article", articleService.getArticleById(id));
        return "nhanvien/article-form";
    }

    // Xử lý cập nhật bài viết
    @PostMapping("/user/bai-viet/update")
    public String updateArticle(@ModelAttribute("article") Article article, @RequestParam("imageFile") MultipartFile imageFile) {
        try {
            // Kiểm tra và xử lý file hình ảnh nếu có thay đổi
            if (!imageFile.isEmpty()) {
                String imageFileName = saveImage(imageFile);
                article.setImageUrl(imageFileName);
            }

            article.setUpdatedAt(new Date());

            // Kiểm tra và đảm bảo rằng comments không phải là null
            if (article.getComments() == null) {
                article.setComments(new ArrayList<>()); // Khởi tạo danh sách rỗng nếu null
            }

            Article existingArticle = articleService.getArticleById(article.getId());
            article.setCreatedAt(existingArticle.getCreatedAt());

            // Cập nhật bài viết
            articleService.updateArticle(article);
            return "redirect:/user/bai-viet";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/user/bai-viet/edit/" + article.getId() + "?error"; // Hiển thị lỗi nếu có
        }
    }

    private String saveImage(MultipartFile imageFile) throws IOException {
        // Kiểm tra và tạo thư mục nếu chưa tồn tại
        Path path = Paths.get(uploadDir, imageFile.getOriginalFilename());
        Files.createDirectories(path.getParent());

        // Lưu hình ảnh vào thư mục
        Files.write(path, imageFile.getBytes());

        // Trả về tên file để lưu vào cơ sở dữ liệu
        return imageFile.getOriginalFilename();
    }

    // Xóa bài viết
    @GetMapping("/user/bai-viet/delete/{id}")
    public String deleteArticle(@PathVariable Integer id) {
        Article article = articleService.findById(id).orElseThrow(() -> new EntityNotFoundException("Bài Viết không tồn tại"));
        // Xóa các comment thuộc về bài viết
        for (Comment comment : article.getComments()) {
            comment.setArticle(null); // Gỡ bỏ liên kết với bài viết
        }
        articleService.delete(article); // Xóa bài viết
        return "redirect:/user/bai-viet";
    }

    @Autowired
    private CommentService commentService;

    @GetMapping("/user/bai-viet/{articleId}/binh-luan")
    public String showComments(@PathVariable Integer articleId, Model model) {
        try {
            Article article = articleService.getArticleById(articleId);
            List<Comment> comments = commentService.getCommentsByArticle(article);

            model.addAttribute("article", article);
            model.addAttribute("comments", comments);

            return "nhanvien/comment-list";
        } catch (EntityNotFoundException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "error/404";  // Trang lỗi 404
        }
    }

    @GetMapping("/user/bai-viet/{articleId}/binh-luan/delete/{id}")
    public String deleteComment(@PathVariable Integer articleId, @PathVariable Integer id, Model model) {
        try {
            // Xóa bình luận theo ID
            commentService.deleteCommentById(id);

            // Chuyển hướng về danh sách bình luận của bài viết
            return "redirect:/user/bai-viet/" + articleId + "/binh-luan";
        } catch (EntityNotFoundException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "error/404"; // Trang lỗi 404
        }
    }

}