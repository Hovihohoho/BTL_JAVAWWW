package iuh.btl_n7_iuh.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    @GetMapping("/cod")
    public String checkoutCOD() {
        return "checkout-cod";
    }

    @GetMapping("/online")
    public String checkoutOnline() {
        return "checkout-online";
    }

    @GetMapping("/vnpay")
    public String payVnpay() {
        return "checkout-vnpay";
    }

    @GetMapping("/momo")
    public String payMomo() {
        return "checkout-momo";
    }

    @GetMapping("/success")
    public String success() {
        return "checkout-success";
    }

    @GetMapping("/checkout/cod")
    public String codCheckout(@RequestParam("amount") Long amount, Model model) {
        model.addAttribute("amount", amount);
        return "checkout-success"; // hoặc checkout-cod.html
    }

    @GetMapping("/checkout/online")
    public String checkoutOnline(@RequestParam("amount") Long amount, Model model) {
        model.addAttribute("amount", amount);
        return "checkout-online";
    }

}

