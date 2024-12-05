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

    public NhanVienService(NhanVienRepo nhanVienRepo, KhachHangRepository khachHangRepository) {
        this.nhanVienRepo = nhanVienRepo;
        this.khachHangRepository = khachHangRepository;
    }

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            Optional<NhanVien> optionalNhanVien = nhanVienRepo.findByUsername(username);
            if (optionalNhanVien.isPresent()) {
                NhanVien nhanVien = optionalNhanVien.get();
                System.out.println("Found NhanVien: " + nhanVien.getUsername());

                GrantedAuthority authority = new SimpleGrantedAuthority(nhanVien.getRole());
                return new User(
                        nhanVien.getUsername(),
                        nhanVien.getPassword(),
                        Collections.singletonList(authority)
                );
            }

            // Tìm trong bảng KhachHang
            Optional<KhachHang> optionalKhachHang = khachHangRepository.findByTaiKhoan(username);
            if (optionalKhachHang.isPresent()) {
                KhachHang khachHang = optionalKhachHang.get();
                System.out.println("Found KhachHang: " + khachHang.getTaiKhoan());

                GrantedAuthority authority = new SimpleGrantedAuthority(khachHang.getRole());
                return new User(
                        khachHang.getTaiKhoan(),
                        khachHang.getMatKhau(),
                        Collections.singletonList(authority)
                );
            }
            throw new UsernameNotFoundException("Username not found: " + username);
        }

    public Integer getLoggedInNhanVienId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetails) {
                String username = ((UserDetails) principal).getUsername();
                Optional<NhanVien> nhanVien = nhanVienRepo.findByUsername(username);
                if (nhanVien.isPresent()) {
                    return nhanVien.get().getId();
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
}
