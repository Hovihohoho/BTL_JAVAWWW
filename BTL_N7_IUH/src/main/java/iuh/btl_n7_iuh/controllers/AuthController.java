package iuh.btl_n7_iuh.controllers;

import iuh.btl_n7_iuh.entities.Account; // Cập nhật import
import iuh.btl_n7_iuh.services.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AccountService userService;

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("account", new Account()); // Cập nhật attribute
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute Account account,
                               @RequestParam String fullname,
                               @RequestParam String phone,
                               @RequestParam String address,
                               Model model) {
        log.info("Registering account: {}", account.getUsername());
        try {
            userService.registerNewUser(account, fullname, phone, address);
            return "redirect:/login?success";
        } catch (Exception e) {
            log.error("Error registering user", e);
            model.addAttribute("error", "Đăng ký thất bại. Vui lòng thử lại.");
            return "register";
        }
    }


    @GetMapping("/forgot-password")
    public String showForgotPasswordPage() {
        return "forgot-password";
    }

    @GetMapping("/change-password")
    public String showChangePasswordPage() {
        return "change-password";
    }
}