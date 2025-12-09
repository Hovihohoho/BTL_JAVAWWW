package iuh.btl_n7_iuh.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CartController {

    @GetMapping("/cart")
    public String cartPage(Model model) {
        return "cart";  // => templates/cart.html
    }
}
