package com.example.demo.service;

import com.example.demo.entity.KhachHang;
import com.example.demo.entity.NhanVien;
import com.example.demo.repository.KhachHangRepository;
import com.example.demo.repository.NhanVienRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class NhanVienService implements UserDetailsService{
//    implements UserDetailsService

    @Autowired
    private final NhanVienRepo nhanVienRepo;

    @Autowired
    private final KhachHangRepository khachHangRepository;

    // Lấy danh sách tất cả nhân viên
    public List<NhanVien> getAllNhanViens() {
        return nhanVienRepo.findAll(); // Trả về danh sách tất cả nhân viên
    }
    public void saveNhanVien(NhanVien nhanVien) {
        nhanVienRepo.save(nhanVien); // Lưu nhân viên vào cơ sở dữ liệu
    }

    // Phương thức lấy thông tin nhân viên theo id
    public NhanVien layNhanVienTheoId(Integer id) {
        return nhanVienRepo.findById(id).orElse(null);
    }

    // Phương thức cập nhật thông tin nhân viên
    public void suaNhanVien(NhanVien nhanVien) {
        nhanVienRepo.save(nhanVien); // Lưu thông tin nhân viên đã sửa
    }
    public List<String> getAllRoles() {
        List<String> roles = nhanVienRepo.findAllRoles(); // Truy vấn tất cả vai trò từ bảng
        return roles;
    }
    // Xóa nhân viên theo ID
    public void xoaNhanVien(Integer id) {
        // Kiểm tra nếu nhân viên tồn tại
        Optional<NhanVien> nhanVienOptional = nhanVienRepo.findById(id);
        if (nhanVienOptional.isPresent()) {
            nhanVienRepo.deleteById(id); // Xóa nhân viên
        }
    }

    public NhanVienService(NhanVienRepo nhanVienRepo, KhachHangRepository khachHangRepository) {
        this.nhanVienRepo = nhanVienRepo;
        this.khachHangRepository = khachHangRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Tìm trong bảng NhanVien trước
        Optional<NhanVien> optionalNhanVien = nhanVienRepo.findByUsername(username);
        if (optionalNhanVien.isPresent()) {
            NhanVien nhanVien = optionalNhanVien.get();
        //    log.info("Found NhanVien: {}", nhanVien.getUsername());

            GrantedAuthority authority = new SimpleGrantedAuthority(nhanVien.getRole());
            return new User(
                    nhanVien.getUsername(),
                    nhanVien.getPassword(),
                    Collections.singletonList(authority)
            );
        }

        // Nếu không tìm thấy, tìm trong bảng KhachHang
        Optional<KhachHang> optionalKhachHang = khachHangRepository.findByTaiKhoan(username);
        if (optionalKhachHang.isPresent()) {
            KhachHang khachHang = optionalKhachHang.get();
       //     log.info("Found KhachHang: {}", khachHang.getTaiKhoan());

            GrantedAuthority authority = new SimpleGrantedAuthority(khachHang.getRole());
            return new User(
                    khachHang.getTaiKhoan(),
                    khachHang.getMatKhau(),
                    Collections.singletonList(authority)
            );
        }

        // Nếu không tìm thấy trong cả hai bảng
      //  log.error("Username not found: {}", username);
        throw new UsernameNotFoundException("Username not found: " + username);
    }


    public Integer getLoggedInNhanVienId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetails) {
                String username = ((UserDetails) principal).getUsername();

                // Kiểm tra trong bảng NhanVien
                Optional<NhanVien> nhanVien = nhanVienRepo.findByUsername(username);
                if (nhanVien.isPresent()) {
                    return nhanVien.get().getId();
                }

                // Kiểm tra trong bảng KhachHang
                Optional<KhachHang> khachHang = khachHangRepository.findByTaiKhoan(username);
                if (khachHang.isPresent()) {
                    return khachHang.get().getId();
                }
            }
        }
            return null;
    }

    public List<NhanVien> layTatCaNhanVien() {
        return nhanVienRepo.findAll(); // Lấy tất cả nhân viên từ cơ sở dữ liệu
    }
    public NhanVien findById(Integer id) {
        return nhanVienRepo.findById(id).orElse(null); // Tìm nhân viên trong cơ sở dữ liệu
    }

    public Optional<NhanVien> findByTaiKhoan(String username) {
        return nhanVienRepo.findByUsername(username);
    }

    public void save(NhanVien nhanVien) {
        nhanVienRepo.save(nhanVien);
    }
}
