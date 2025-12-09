package iuh.btl_n7_iuh.controllers;

import iuh.btl_n7_iuh.dto.CartItem;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class CartController {

    @GetMapping("/cart")
    public String cartPage(Model model) {
        return "cart";  // => templates/cart.html
    }

    @PostMapping("/api/cart/sync")
    @ResponseBody
    public ResponseEntity<String> syncCart(@RequestBody List<CartItem> cartItems, HttpSession session) {
        session.setAttribute("cart", cartItems); // Lưu vào Session để CheckoutController đọc
        return ResponseEntity.ok("Synched successfully");
    }
}
