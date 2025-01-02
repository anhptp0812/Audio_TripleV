package com.example.demo;//package com.example.demo;

import com.example.demo.auth.CustomAuthenticationSuccessHandler;
import com.example.demo.service.NhanVienService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final NhanVienService userDetailsService;

    public SecurityConfig(NhanVienService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults(""); // Không thêm tiền tố "ROLE_"
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity, PasswordEncoder passwordEncoder) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
        return authenticationManagerBuilder.build();  // Trả về AuthenticationManager trực tiếp
    }
    @Bean
    public CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // Tắt CSRF (nếu cần)
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/static/**", "/css/**", "/js/**", "/images/**").permitAll()
//                        .requestMatchers("/trang-chu/hien-thi/**", "/san-pham/hien-thi/**").permitAll()
                        .requestMatchers("/admin/**").hasAuthority("ADMIN")  // Chỉ ADMIN truy cập
                        .requestMatchers("/user/**").hasAuthority("USER")
                        .requestMatchers("/khach-hang/**").hasAuthority("KHACHHANG")
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage("/login")  // Đảm bảo URL đăng nhập hợp lệ
                        .successHandler(customAuthenticationSuccessHandler()) // Chỉ định trang thành công sau khi đăng nhập
                        .failureUrl("/login?error=true")  // Đường dẫn nếu đăng nhập thất bại
                        .permitAll()  // Cho phép tất cả truy cập vào trang đăng nhập
                );

        return http.build();

    }
}

