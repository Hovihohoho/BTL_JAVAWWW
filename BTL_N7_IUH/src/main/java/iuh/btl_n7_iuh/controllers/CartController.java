package iuh.btl_n7_iuh.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CartController {

    @GetMapping("/checkout")
    public String checkoutPage(Model model) {
        // có thể truyền thêm tổng tiền, danh sách sản phẩm
        return "checkout";
    }
}
