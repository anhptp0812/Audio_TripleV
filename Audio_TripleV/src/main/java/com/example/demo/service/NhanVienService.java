package com.example.demo.service;

import com.example.demo.entity.NhanVien;
import com.example.demo.repository.NhanVienRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NhanVienService implements UserDetailsService{
//    implements UserDetailsService

    @Autowired
    private final NhanVienRepo nhanVienRepo;

    public NhanVienService(NhanVienRepo nhanVienRepo) {
        this.nhanVienRepo = nhanVienRepo;
    }

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            System.out.println("Trying to authenticate user: " + username);
            NhanVien nhanVien = nhanVienRepo.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
            System.out.println("Found user: " + nhanVien.getUsername());
            return org.springframework.security.core.userdetails.User.builder()
                    .username(nhanVien.getUsername())
                    .password(nhanVien.getPassword())
                    .roles(nhanVien.getRole()).build();
        }

    public List<NhanVien> layTatCaNhanVien() {
        return nhanVienRepo.findAll(); // Lấy tất cả nhân viên từ cơ sở dữ liệu
    }

}
