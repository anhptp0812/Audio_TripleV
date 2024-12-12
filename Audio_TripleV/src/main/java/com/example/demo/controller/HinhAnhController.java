package com.example.demo.controller;

import com.example.demo.entity.HinhAnh;
import com.example.demo.service.HinhAnhService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

@Controller
@RequestMapping("/hinh-anh")
public class HinhAnhController {

    @Autowired
    private HinhAnhService hinhAnhService;

    @GetMapping("/hien-thi")
    public String hienThi(Model model) {
        model.addAttribute("hinhAnhs", hinhAnhService.findAll());
        return "admin/hinh-anh";
    }

    @Value("${upload.dir}")
    private String uploadDir;

    // Hiển thị form thêm màu sắc
    @GetMapping("/form-add")
    public String add() {
        return "admin/hinh-anh-add";
    }

    // Xử lý thêm hình ảnh bằng JSON cho AJAX
    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<String> addHinhAnh(@RequestParam("imageFile") MultipartFile imageFile) {
        try {
            // Tạo đối tượng HinhAnh mới
            HinhAnh hinhAnh = new HinhAnh();
            String fileName = imageFile.getOriginalFilename();
            hinhAnh.setTen(fileName); // Gán tên hình ảnh bằng tên file
            hinhAnh.setNgayTao(new Date());
            hinhAnh.setNgayCapNhat(new Date());

            // Đường dẫn tuyệt đối trong thư mục static
            Path path = Paths.get(uploadDir, fileName);

            // Kiểm tra và tạo thư mục nếu chưa tồn tại
            Files.createDirectories(path.getParent());

            // Lưu file vào thư mục
            Files.write(path, imageFile.getBytes());

            // Lưu thông tin vào database
            hinhAnhService.save(hinhAnh);

            return ResponseEntity.ok("Thêm hình ảnh thành công!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Có lỗi xảy ra khi thêm hình ảnh.");
        }
    }

    @GetMapping("/form-update/{id}")
    public String showUpdateForm(@PathVariable Integer id, Model model) {
        HinhAnh hinhAnh = hinhAnhService.findById(id);
        model.addAttribute("hinhAnh", hinhAnh);
        return "admin/hinh-anh-update";
    }

    @PutMapping("/update")
    @ResponseBody
    public ResponseEntity<String> updateHinhAnh(@RequestParam("id") Integer id,
                                                @RequestParam("ten") String ten,
                                                @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
        try {
            // Tìm hình ảnh cần cập nhật trong cơ sở dữ liệu
            HinhAnh hinhAnh = hinhAnhService.findById(id);
            if (hinhAnh == null) {
                return ResponseEntity.status(404).body("Hình ảnh không tồn tại.");
            }

            // Cập nhật thông tin tên hình ảnh và ngày cập nhật
            hinhAnh.setTen(ten);
            hinhAnh.setNgayCapNhat(new Date());

            // Nếu có file hình ảnh mới, xử lý việc lưu trữ
            if (imageFile != null && !imageFile.isEmpty()) {
                // Xóa tệp hình ảnh cũ nếu cần thiết
                Path oldFilePath = Paths.get(uploadDir, hinhAnh.getTen());
                if (Files.exists(oldFilePath)) {
                    Files.delete(oldFilePath);
                }

                // Tạo đường dẫn và lưu file mới
                String newFileName = imageFile.getOriginalFilename();
                Path newPath = Paths.get(uploadDir, newFileName);

                // Kiểm tra và tạo thư mục nếu chưa có
                Files.createDirectories(newPath.getParent());

                // Lưu file mới
                Files.write(newPath, imageFile.getBytes());

                // Cập nhật tên hình ảnh trong đối tượng HinhAnh
                hinhAnh.setTen(newFileName);
            }

            // Cập nhật hình ảnh vào cơ sở dữ liệu
            hinhAnhService.updateHinhAnh(hinhAnh);

            // Trả về phản hồi thành công
            return ResponseEntity.ok("Cập nhật hình ảnh thành công!");
        } catch (Exception e) {
            // Trả về lỗi nếu có ngoại lệ
            return ResponseEntity.status(500).body("Có lỗi xảy ra khi cập nhật hình ảnh: " + e.getMessage());
        }
    }

    // Xóa hình ảnh
    @GetMapping("/delete/{id}")
    public String deleteHinhAnh(@PathVariable Integer id) {
        hinhAnhService.deleteById(id);
        return "redirect:/hinh-anh/hien-thi"; // Thêm tham số activated
    }

}
