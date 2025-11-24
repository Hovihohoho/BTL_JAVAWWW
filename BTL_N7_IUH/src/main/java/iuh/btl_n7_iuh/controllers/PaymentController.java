package iuh.btl_n7_iuh.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping("/checkout")
@Controller
public class PaymentController {
    @GetMapping("/checkout/success")
    public String success() {
        return "checkout-success";
    }
}

