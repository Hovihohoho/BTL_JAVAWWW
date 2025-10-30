package iuh.btl_n7_iuh.controllers.admin;

import iuh.btl_n7_iuh.entities.Account;
import iuh.btl_n7_iuh.repositories.AccountRepository;
//import iuh.btl_n7_iuh.repositories.ContactRepository; // Giả sử bạn có ContactRepository (cho Quản lý Liên hệ)
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AccountRepository accountRepository;
    // Bổ sung các Repository cần thiết cho các màn hình khác
    // private final ProductRepository productRepository;
    // private final CategoryRepository categoryRepository;
    // private final ContactRepository contactRepository;


    /**
     * Màn hình chính Admin (Ảnh chụp màn hình 2025-10-10 102238.jpg)
     */
    @GetMapping({"", "/"})
    public String adminHome() {
        // Trả về view cho trang chủ Admin
        return "admin/dashboard"; // Bạn cần tạo file dashboard.html
    }

    /**
     * Màn hình Quản lý Tài khoản (Ảnh chụp màn hình 2025-10-10 102313.jpg)
     */
    @GetMapping("/accounts")
    public String manageAccounts(Model model) {
        // Lấy tất cả tài khoản
        List<Account> accounts = accountRepository.findAll();
        model.addAttribute("accounts", accounts);
        return "admin/accounts/list"; // Trả về file admin/accounts/list.html
    }

    /**
     * Màn hình Quản lý Sản phẩm (Ảnh chụp màn hình 2025-10-10 102259.jpg)
     */
    @GetMapping("/products")
    public String manageProducts(Model model) {
        // List<Product> products = productRepository.findAll();
        // model.addAttribute("products", products);
        return "admin/products/list"; // Trả về file admin/products/list.html
    }

    // Thêm các hàm khác tương tự cho /admin/categories, /admin/contacts, /admin/banners...
}