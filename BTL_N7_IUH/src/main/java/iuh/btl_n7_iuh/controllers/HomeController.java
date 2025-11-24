package iuh.btl_n7_iuh.controllers;

import iuh.btl_n7_iuh.entities.Product;
import iuh.btl_n7_iuh.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import iuh.btl_n7_iuh.models.CustomUserDetails;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    @Autowired
    private ProductService productServices;
    /**
     * Xử lý yêu cầu GET cho URL gốc ("/") và hiển thị Trang Chủ.
     * Đây là điểm truy cập chính của website.
     */
    @GetMapping("/")
    public String homePage(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Kiểm tra nếu người dùng đã được xác thực (không phải anonymous)
        if (authentication != null && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {

            // Dùng stream để kiểm tra nếu người dùng có quyền ROLE_ADMIN
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

            if (isAdmin) {
                // Nếu là Admin, chuyển hướng đến trang quản trị
                return "redirect:/admin";
            }
        }

        // Lấy danh sách sản phẩm nổi bật từ tầng Service
        // Đảm bảo ProductServices có phương thức getFeaturedProducts()
        List<Product> featuredProducts = productServices.getFeaturedProducts();

        model.addAttribute("products", featuredProducts);


        // Trả về tên view: "index" -> /WEB-INF/jsp/index.jsp
        return "index";
    }

    /**
     * Xử lý yêu cầu GET cho URL đăng nhập ("client/login").
     * Phương thức này hiện chỉ có nhiệm vụ hiển thị form,
     * không có logic xử lý lỗi Security nào.
     */
    @GetMapping("/client/login")
    public String showLoginPage() {
        // Trả về tên view: "login" -> /WEB-INF/jsp/login.jsp
        return "login";
    }

    // Xóa hoặc comment các mapping trùng lặp với controller khác
    // @GetMapping("/contact")
    // public String contactPage(){
    //     return "contact";
    // }
    // @GetMapping("/cart")
    // public String cartPage() {
    //     return "cart";
    // }
}
