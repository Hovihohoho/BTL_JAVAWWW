package iuh.btl_n7_iuh.controllers;

import iuh.btl_n7_iuh.entities.Account;
import iuh.btl_n7_iuh.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProfileController {

    @Autowired
    private AccountService accountService;

    // Hiển thị trang thông tin cá nhân
    @GetMapping("/profile")
    public String showProfile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails == null) return "redirect:/login";

        // Lấy thông tin mới nhất từ DB để hiển thị
        Account account = accountService.findByUsername(userDetails.getUsername());// Username ở đây là email hoặc username tùy config
        if(account == null) {
            // Fallback nếu findByEmail trả null, thử tìm theo username gốc
            // (Tùy thuộc vào cách bạn config UserDetailsService)
            try {
                // Logic tạm: giả sử service có hàm findByUsername trả về Account
                // Nếu không, bạn cần dùng repository trực tiếp hoặc bổ sung service
            } catch (Exception e) {}
        }

        // Truyền account object xuống view
        model.addAttribute("account", account);
        return "profile";
    }

    // Xử lý cập nhật thông tin
    @PostMapping("/profile/update")
    public String updateProfile(@AuthenticationPrincipal UserDetails userDetails,
                                @RequestParam("fullName") String fullName,
                                @RequestParam("phone") String phone,
                                @RequestParam("address") String address,
                                RedirectAttributes redirectAttributes) {

        try {
            accountService.updateProfile(userDetails.getUsername(), fullName, phone, address);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thông tin thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Có lỗi xảy ra khi cập nhật.");
        }

        return "redirect:/profile";
    }
}