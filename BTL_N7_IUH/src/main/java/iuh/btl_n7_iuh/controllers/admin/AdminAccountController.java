package iuh.btl_n7_iuh.controllers.admin;

import iuh.btl_n7_iuh.entities.Account;
import iuh.btl_n7_iuh.services.AccountService;
import iuh.btl_n7_iuh.services.RoleServices;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/accounts")
public class AdminAccountController {

    private final AccountService accountService;
    private final RoleServices roleService;

    // 📌 Hiển thị danh sách user
    @GetMapping
    public String listAccounts(Model model) {
        model.addAttribute("accounts", accountService.findAll());     // trả về List<Account>
        model.addAttribute("roles", roleService.findAll());           // để đổ vào combobox đổi role
        return "admin/accounts/list";
    }

    // 📌 Xóa user
    @PostMapping("/delete/{id}")
    public String deleteAccount(@PathVariable Long id,
                                RedirectAttributes redirectAttributes) {

        boolean deleted = accountService.deleteById(id);

        if (deleted) {
            redirectAttributes.addFlashAttribute(
                    "successMessage", "Xóa tài khoản thành công!"
            );
        } else {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Không thể xóa tài khoản vì đang có dữ liệu liên quan (đơn hàng, bình luận, v.v.)"
            );
        }

        return "redirect:/admin/accounts";
    }

    // 📌 Cập nhật role cho user
    @PostMapping("/update-role/{id}")
    public String updateRole(@PathVariable Long id,
                             @RequestParam("roleId") Long roleId,
                             RedirectAttributes redirectAttributes) {

        boolean updated = accountService.updateRole(id, roleId); // tự hiện thực trong service

        if (updated) {
            redirectAttributes.addFlashAttribute("successMessage",
                    "Cập nhật quyền cho tài khoản thành công!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Không tìm thấy tài khoản hoặc quyền!");
        }

        return "redirect:/admin/accounts";
    }

    // ---- Cập nhật trạng thái theo tham số true/false ----
    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam("enabled") boolean enabled,
                               RedirectAttributes redirectAttributes) {

        boolean ok = accountService.updateEnabled(id, enabled);

        if (ok) {
            redirectAttributes.addFlashAttribute("successMessage",
                    enabled ? "Đã mở khóa tài khoản." : "Đã khóa tài khoản.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Không thể cập nhật trạng thái tài khoản.");
        }

        return "redirect:/admin/accounts";
    }

    // ---- Toggle (không cần truyền tham số) ----
    @PostMapping("/{id}/toggle")
    public String toggle(@PathVariable Long id,
                         RedirectAttributes redirectAttributes) {

        boolean ok = accountService.toggleEnabled(id);

        redirectAttributes.addFlashAttribute(
                ok ? "successMessage" : "errorMessage",
                ok ? "Đã thay đổi trạng thái tài khoản."
                        : "Không tìm thấy tài khoản."
        );

        return "redirect:/admin/accounts";
    }
}
