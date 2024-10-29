// AuthController.java
package com.example.demo.auth;

import com.example.demo.entity.NhanVien;
import com.example.demo.repository.NhanVienRepo;
import com.example.demo.service.NhanVienService;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Controller
public class AuthController {

    @Autowired
    private NhanVienService nhanVienService;

    @Autowired
    private NhanVienRepo nhanVienRepo;

//    @GetMapping("/login")
//    public String index() {
//        return "login/auth";
//    }

//    @GetMapping("/default")
//    public String defaultAfterLogin(Authentication authentication) {
//        if (authentication.getAuthorities().stream().anyMatch(grantedAuthority ->
//                grantedAuthority.equals("ROLE_USER"))){
//            return "redirect:/admin/home";
//        }else {
//            return "redirect:/user/ban-hang";
//        }
//    }

//    @GetMapping("/login")
//    public String form() {
//        return "login/auth";
//    }


//        public String getUserName(Integer id) {
//        Optional<NhanVien> user = nhanVienRepo.findById(id);
//        return user != null ? user.get().getTen() : "Unknown User";
//    }
    @GetMapping("admin/home")
    public String login(Model model, Principal principal) {
//        session.setAttribute("userId", id);
//        Integer userId = (Integer) session.getAttribute("userId");
//        String username = getUserName(userId);
        String username = principal.getName();
        model.addAttribute("userName", username);
        return "admin/trang_chu";
    }

//    @PostMapping("/login")
//    public String login1(@RequestBody NhanVien nv) {
//        return nv.getUsename() nv.getPassword();
//    }

}
