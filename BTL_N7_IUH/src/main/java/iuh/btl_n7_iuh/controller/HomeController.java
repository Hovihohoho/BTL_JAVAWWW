package iuh.btl_n7_iuh.controller;

import iuh.btl_n7_iuh.entities.Product;
import iuh.btl_n7_iuh.services.ProductServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private ProductServices productServices;

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
}