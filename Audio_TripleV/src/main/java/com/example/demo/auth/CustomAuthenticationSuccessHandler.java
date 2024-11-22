package com.example.demo.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // Lấy danh sách quyền (roles) của người dùng
        var authorities = authentication.getAuthorities();

        // Kiểm tra role và chuyển hướng tương ứng
        if (authorities.contains(new SimpleGrantedAuthority("ADMIN"))) {
            response.sendRedirect("/admin/home");
        } else if (authorities.contains(new SimpleGrantedAuthority("USER"))) {
            response.sendRedirect("/user/ban-hang");
        } else if (authorities.contains(new SimpleGrantedAuthority("KHACHHANG"))) {
            response.sendRedirect("/khach-hang/trang-chu/hien-thi");
        } else {
            // Chuyển hướng mặc định nếu không tìm thấy role phù hợp
            response.sendRedirect("/default");
        }
    }
}
