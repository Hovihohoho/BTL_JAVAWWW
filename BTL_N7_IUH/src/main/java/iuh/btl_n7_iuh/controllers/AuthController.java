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


    @PostMapping("/forgot-password")
    public String handleForgotPassword(@RequestParam("email") String email, Model model) {

        try {
            String token = java.util.UUID.randomUUID().toString();

            userService.updateResetToken(token, email);

            String resetLink = "http://localhost:8080/reset-password?token=" + token;

            // (DEBUG) hiển thị trực tiếp link reset để test
            log.info("LINK RESET PASSWORD: {}", resetLink);

            model.addAttribute("message",
                    "Liên kết đặt lại mật khẩu đã được gửi! (debug link: " + resetLink + ")");

            return "forgot-password-success";  // Tạo file này hoặc redirect với ?success

        } catch (Exception e) {
            log.error("Forgot password error: ", e);
            return "redirect:/forgot-password?error";
        }
    }



    @PostMapping("/change-password")
    public String handleChangePassword(
            @RequestParam("currentPassword") String currentPassword,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword) {

        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            return "redirect:/change-password?error";
        }

        if (!newPassword.equals(confirmPassword)) {
            return "redirect:/change-password?notmatch";
        }

        // TODO: logic đổi mật khẩu (khi bạn muốn)
        log.info("Mật khẩu mới hợp lệ. Chuẩn bị đổi mật khẩu...");

        return "redirect:/change-password?success";
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordPage() {
        return "forgot-password"; // trỏ đến file forgot-password.html
    }

    @GetMapping("/change-password")
    public String showChangePasswordPage() {
        return "change-password";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordPage(@RequestParam("token") String token, Model model) {

        Account account = userService.getByResetToken(token);

        if (account == null) {
            model.addAttribute("error", "Invalid token");
            return "reset-password";
        }

        model.addAttribute("token", token);
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String handleResetPassword(@RequestParam("token") String token,
                                      @RequestParam("newPassword") String newPassword,
                                      Model model) {

        Account account = userService.getByResetToken(token);

        if (account == null) {
            model.addAttribute("error", "Invalid token");
            return "reset-password";
        }

        userService.updatePassword(account, newPassword);

        model.addAttribute("success", "Đặt lại mật khẩu thành công!");
        return "reset-password";
    }





}