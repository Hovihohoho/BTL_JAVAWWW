package iuh.btl_n7_iuh.controllers;

import iuh.btl_n7_iuh.dto.CartItem;
import iuh.btl_n7_iuh.entities.Account;
import iuh.btl_n7_iuh.repositories.AccountRepository;
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
    private final AccountRepository accountRepository;

    @GetMapping("/cod")
    public String processCOD(@RequestParam(value = "amount", required = false) Long amount,
                             HttpServletRequest request,
                             Model model) {
        try {
            // 1. Kiểm tra đăng nhập
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || authentication.getName().equals("anonymousUser")) {
                return "redirect:/login";
            }
            String username = authentication.getName();

            // 2. Lấy giỏ hàng
            List<CartItem> cartItems = cartService.getCartItemsFromSession(request);
            if (cartItems == null || cartItems.isEmpty()) {
                throw new RuntimeException("Giỏ hàng trống (Session Cart is null/empty)");
            }

            // 3. Lấy địa chỉ
            String address = "Chưa có địa chỉ";
            Optional<Account> accountOpt = accountRepository.findByUsername(username);
            if (accountOpt.isPresent() && accountOpt.get().getAccountDetail() != null) {
                address = accountOpt.get().getAccountDetail().getAddress();
            }

            // 4. Tính tiền
            BigDecimal totalAmount = (amount != null) ? BigDecimal.valueOf(amount) : BigDecimal.ZERO;

            // 5. Tạo đơn
            orderService.createOrder(username, totalAmount, "COD", address, cartItems);

            // 6. Xóa giỏ
            cartService.clearCartSession(request);

            return "checkout-cod";

        } catch (Exception e) {
            e.printStackTrace();
            // In lỗi chi tiết ra màn hình để bạn chụp ảnh gửi tôi
            model.addAttribute("errorMessage", "LỖI CHI TIẾT: " + e.getMessage() + " | Class: " + e.getClass().getName());
            return "checkout-fail";
        }
    }
}