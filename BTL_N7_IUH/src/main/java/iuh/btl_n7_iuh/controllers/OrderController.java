package iuh.btl_n7_iuh.controllers;

import iuh.btl_n7_iuh.entities.Order;
import iuh.btl_n7_iuh.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import iuh.btl_n7_iuh.security.CustomUserDetails;

import java.util.List;

@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/orders")
    public String viewOrders(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        String username = userDetails.getUsername();
        List<Order> orders = orderService.getOrdersByUsername(username);

        model.addAttribute("orders", orders);
        return "orders"; // ✅ trỏ tới file orders.html
    }
}
