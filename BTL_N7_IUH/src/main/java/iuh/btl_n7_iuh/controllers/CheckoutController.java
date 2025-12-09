package iuh.btl_n7_iuh.controllers;

import iuh.btl_n7_iuh.dto.CartItem;
import iuh.btl_n7_iuh.entities.Account;
import iuh.btl_n7_iuh.repositories.AccountRepository; // ✅ Thêm import này
import iuh.btl_n7_iuh.services.CartService;
import iuh.btl_n7_iuh.services.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/checkout")
@RequiredArgsConstructor
public class CheckoutController {

    private final OrderService orderService;
    private final CartService cartService;
    private final AccountRepository accountRepository; // ✅ Inject thêm AccountRepository để lấy địa chỉ

    @GetMapping("/cod")
    public String processCOD(@RequestParam(value = "amount", required = false) Long amount,
                             HttpServletRequest request,
                             Model model) {

        // 1. Kiểm tra đăng nhập
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName().equals("anonymousUser")) {
            return "redirect:/login";
        }
        String username = authentication.getName();

        // 2. Lấy giỏ hàng từ Session
        List<CartItem> cartItems = cartService.getCartItemsFromSession(request);

        if (cartItems == null || cartItems.isEmpty()) {
            model.addAttribute("errorMessage", "Giỏ hàng trống! Vui lòng quay lại mua hàng.");
            return "checkout-fail";
        }

        // 3. Lấy địa chỉ giao hàng từ tài khoản (Sửa lỗi logic cũ)
        String address = "Địa chỉ mặc định";
        Optional<Account> accountOpt = accountRepository.findByUsername(username);
        if (accountOpt.isPresent() && accountOpt.get().getAccountDetail() != null) {
            address = accountOpt.get().getAccountDetail().getAddress();
        }

        // 4. Xử lý tổng tiền (nếu amount trên URL bị null thì tự tính lại)
        BigDecimal totalAmount;
        if (amount != null) {
            totalAmount = BigDecimal.valueOf(amount);
        } else {
            double sum = cartItems.stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();
            totalAmount = BigDecimal.valueOf(sum + 15000); // Cộng phí ship 15k
        }

        try {
            // 5. Gọi Service tạo đơn hàng
            orderService.createOrder(
                    username,
                    totalAmount,
                    "COD",
                    address,
                    cartItems
            );

            // 6. Xóa giỏ hàng sau khi thành công
            cartService.clearCartSession(request);

            return "checkout-cod"; // Chuyển đến trang thành công

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Lỗi hệ thống: " + e.getMessage());
            return "checkout-fail";
        }
    }

    // Các endpoint khác giữ nguyên
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
}