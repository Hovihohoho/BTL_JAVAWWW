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

    @GetMapping("/contact")
    public String contactPage(){
        return "contact";
    }

    @GetMapping("/cart")
    public String cartPage() {
        return "cart";
    }
}