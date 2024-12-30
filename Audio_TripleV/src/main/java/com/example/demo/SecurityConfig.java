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
                .csrf(csrf -> csrf.disable())  // Tắt CSRF nếu không cần
                .authorizeHttpRequests(authz -> authz
                        // Các URL public
                        .requestMatchers( "/static/**", "/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/khach-hang/trang-chu/hien-thi", "/khach-hang/san-pham/hien-thi/**",
                                "/khach-hang/bai-viet/**", "khach-hang/lien-he/**").permitAll()
                        // Các URL cần quyền cụ thể
                        .requestMatchers("/admin/**").hasAuthority("ADMIN")
                        .requestMatchers("/user/**").hasAuthority("USER")
                        .requestMatchers("/khach-hang/**").hasAuthority("KHACHHANG")

                        // Tất cả các yêu cầu khác cần xác thực
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage("/login")  // Đường dẫn trang đăng nhập
                        .successHandler(customAuthenticationSuccessHandler())  // Xử lý khi đăng nhập thành công
                        .failureUrl("/login?error=true")  // Xử lý khi đăng nhập thất bại
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // URL logout
                        .logoutSuccessUrl("/khach-hang/trang-chu/hien-thi") // Chuyển hướng về trang chủ sau khi logout
                        .permitAll() // Cho phép tất cả truy cập vào trang logout
                );


        return http.build();
    }


    }


