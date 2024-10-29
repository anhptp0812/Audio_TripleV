//package com.example.demo;
//
//import com.example.demo.service.NhanVienService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    @Autowired
//    private NhanVienService nhanVienService;
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
//        return authConfig.getAuthenticationManager();
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(req -> req
//                        .requestMatchers("/admin/**").hasRole("ADMIN") // Kiểm tra quyền ADMIN
//                        .requestMatchers("/user/**").hasAnyRole("ADMIN","USER")
//                        .requestMatchers("/images/**").permitAll()
//                        .anyRequest().permitAll() // Các yêu cầu khác không yêu cầu phân quyền
//                )
//                .formLogin(login -> login
//                        //.loginPage("/login") // Trang đăng nhập
//                        .loginProcessingUrl("/login") // URL xử lý đăng nhập
//                        .defaultSuccessUrl("/default", true)
//                       // .successForwardUrl("/user/trang-chu")// Chuyển hướng khi đăng nhập thành công
//                        .passwordParameter("password")
//                        .usernameParameter("username")
//
//                )
//
//                .logout(logout -> logout
//                        .logoutUrl("/logout") // URL xử lý đăng xuất
//                        .logoutSuccessUrl("/auth") // Chuyển hướng khi đăng xuất thành công
//                );
//
//
//
//        return http.build();
//    }
//}
