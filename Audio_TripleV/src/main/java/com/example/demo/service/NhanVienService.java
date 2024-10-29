package com.example.demo.service;

import com.example.demo.entity.NhanVien;
import com.example.demo.repository.NhanVienRepo;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class NhanVienService  {
//    implements UserDetailsService

    @Autowired
    private NhanVienRepo nhanVienRepo;

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Optional<NhanVien> nhanVienOptional = nhanVienRepo.findByTenDangNhap(username);
//
//        if (!nhanVienOptional.isPresent()) {
//            throw new UsernameNotFoundException("User not found");
//        }
//
//        NhanVien nhanVien = nhanVienOptional.get();
//
//        // Log vai trò của người dùng
//        System.out.println("Vai trò của người dùng: " + nhanVien.getRoll());
//
//        Collection<GrantedAuthority> authorities = new ArrayList<>();
//        authorities.add(new SimpleGrantedAuthority(nhanVien.getRoll())); // Lấy vai trò từ database
//
//        return new User(nhanVien.getUsename(), nhanVien.getPassword(), authorities);
//    }

    public List<NhanVien> layTatCaNhanVien() {
        return nhanVienRepo.findAll(); // Lấy tất cả nhân viên từ cơ sở dữ liệu
    }


//

}
