package com.example.demo.service;

import com.example.demo.entity.NhanVien;
import com.example.demo.repository.NhanVienRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
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

            GrantedAuthority authority = new SimpleGrantedAuthority(nhanVien.getRole());

            return new User(
                    nhanVien.getUsername(),
                    nhanVien.getPassword(),
                    Collections.singletonList(authority)
            );
        }

    public List<NhanVien> layTatCaNhanVien() {
        return nhanVienRepo.findAll(); // Lấy tất cả nhân viên từ cơ sở dữ liệu
    }

}
