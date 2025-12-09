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

    // ================== DANH SÁCH TÀI KHOẢN ==================
    @GetMapping
    public String listAccounts(Model model) {
        model.addAttribute("accounts", accountService.findAll());     // List<Account>
        model.addAttribute("roles", roleService.findAll());           // cho combobox role
        return "admin/accounts/list";
    }

    // ================== THÊM TÀI KHOẢN ==================

    // Mở form thêm mới
    @GetMapping("/add")
    public String showAddForm(Model model) {
        // nếu chưa có attribute "account" (khi redirect lỗi) thì tạo mới
        if (!model.containsAttribute("account")) {
            model.addAttribute("account", new Account());
        }
        model.addAttribute("roles", roleService.findAll());
        return "admin/accounts/add-edit";
    }

    // Submit form thêm mới
    @PostMapping("/add")
    public String handleAdd(
            @ModelAttribute("account") Account account,
            @RequestParam("roleId") Long roleId,
            @RequestParam(value = "rawPassword", required = false) String rawPassword,
            @RequestParam(value = "enabled", required = false) Boolean enabled,
            RedirectAttributes redirectAttributes
    ) {
        try {
            // nếu không gửi enabled thì default = true
            if (enabled == null) {
                enabled = Boolean.TRUE;
            }
            account.setIsEnabled(enabled);

            accountService.createAccount(account, roleId, rawPassword);

            redirectAttributes.addFlashAttribute("successMessage",
                    "Thêm tài khoản mới thành công!");
            return "redirect:/admin/accounts";
        } catch (Exception ex) {
            ex.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Không thể tạo tài khoản: " + ex.getMessage());
            redirectAttributes.addFlashAttribute("account", account);
            return "redirect:/admin/accounts/add";
        }
    }

    // ================== SỬA TÀI KHOẢN ==================

    // Mở form edit
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model,
                               RedirectAttributes redirectAttributes) {

        Account account = accountService.findById(id);
        if (account == null) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Không tìm thấy tài khoản với ID = " + id);
            return "redirect:/admin/accounts";
        }

        // Nếu trước đó redirect lỗi và đã có "account" thì không ghi đè
        if (!model.containsAttribute("account")) {
            model.addAttribute("account", account);
        }
        model.addAttribute("roles", roleService.findAll());

        return "admin/accounts/add-edit";
    }

    // ================== SỬA TÀI KHOẢN ==================

    // Submit form edit
    @PostMapping("/edit/{id}")
    public String handleEdit(
            @PathVariable Long id,
            @ModelAttribute("account") Account formAccount,
            @RequestParam("roleId") Long roleId,
            @RequestParam(value = "rawPassword", required = false) String rawPassword,
            @RequestParam(value = "enabled", required = false) Boolean enabled,
            RedirectAttributes redirectAttributes
    ) {
        try {
            if (enabled != null) {
                formAccount.setIsEnabled(enabled);
            }

            boolean ok = accountService.updateAccount(id, formAccount, roleId, rawPassword);

            if (ok) {
                redirectAttributes.addFlashAttribute("successMessage",
                        "Cập nhật tài khoản thành công!");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage",
                        "Không tìm thấy tài khoản cần cập nhật!");
            }
            return "redirect:/admin/accounts";

        } catch (Exception ex) {
            ex.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Không thể cập nhật tài khoản: " + ex.getMessage());
            redirectAttributes.addFlashAttribute("account", formAccount);
            return "redirect:/admin/accounts/edit/" + id;
        }
    }


    // ================== XÓA TÀI KHOẢN ==================
    @PostMapping("/delete/{id}")
    public String deleteAccount(@PathVariable Long id,
                                RedirectAttributes redirectAttributes) {
        try {
            accountService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Xoá tài khoản thành công!");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }

        return "redirect:/admin/accounts";
    }


    // ================== CẬP NHẬT ROLE ==================
    @PostMapping("/update-role/{id}")
    public String updateRole(@PathVariable Long id,
                             @RequestParam("roleId") Long roleId,
                             RedirectAttributes redirectAttributes) {

        boolean updated = accountService.updateRole(id, roleId);

        if (updated) {
            redirectAttributes.addFlashAttribute("successMessage",
                    "Cập nhật quyền cho tài khoản thành công!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Không tìm thấy tài khoản hoặc quyền!");
        }

        return "redirect:/admin/accounts";
    }

    // ================== CẬP NHẬT TRẠNG THÁI ==================
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

    // Toggle nhanh (nếu còn dùng)
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
