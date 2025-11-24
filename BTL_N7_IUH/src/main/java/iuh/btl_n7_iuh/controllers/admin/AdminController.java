package iuh.btl_n7_iuh.controllers.admin;

import iuh.btl_n7_iuh.entities.Account;
import iuh.btl_n7_iuh.entities.Category; // Import Category
import iuh.btl_n7_iuh.entities.Product; // Import Product
import iuh.btl_n7_iuh.repositories.AccountRepository;
import iuh.btl_n7_iuh.repositories.CategoryRepository; // Import CategoryRepository
import iuh.btl_n7_iuh.repositories.ProductRepository; // Import ProductRepository
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
    private final CategoryRepository categoryRepository; // Inject CategoryRepository
    private final ProductRepository productRepository; // Inject ProductRepository

    @GetMapping({"", "/"})
    public String adminHome(Model model) {
        model.addAttribute("title", "Dashboard Admin - Frubana");
        return "admin/dashboard";
    }

    /**
     * Màn hình Quản lý Tài khoản
     */
    @GetMapping("/accounts")
    public String manageAccounts(Model model) {
        List<Account> accounts = accountRepository.findAll();
        model.addAttribute("accounts", accounts);
        model.addAttribute("title", "Quản lý Tài khoản - Frubana");
        return "admin/accounts/list";
    }

    /**
     * Màn hình Quản lý Danh mục (MỚI)
     */
    @GetMapping("/categories")
    public String manageCategories(Model model) {
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories); // Truyền danh sách danh mục
        model.addAttribute("title", "Quản lý Danh mục - Frubana");
        return "admin/categories/list";
    }

    /**
     * Màn hình Quản lý Sản phẩm (MỚI)
     */
    @GetMapping("/products")
    public String manageProducts(Model model) {
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products); // Truyền danh sách sản phẩm
        model.addAttribute("title", "Quản lý Sản phẩm - Frubana");
        return "admin/products/list";
    }
}