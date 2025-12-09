package iuh.btl_n7_iuh.controllers;

import iuh.btl_n7_iuh.dto.Cart;
import iuh.btl_n7_iuh.dto.CartItem;
import iuh.btl_n7_iuh.entities.*;
import iuh.btl_n7_iuh.repositories.OrderDetailRepository;
import iuh.btl_n7_iuh.repositories.OrderRepository;
import iuh.btl_n7_iuh.repositories.OrderStatusRepository;
import iuh.btl_n7_iuh.services.OrderService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@Controller
@RequestMapping("/checkout")
@RequiredArgsConstructor
public class CheckoutController {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final OrderStatusRepository orderStatusRepository;

    private final OrderService orderService;

    @GetMapping("")
    public String checkoutPage() {
        return "checkout";   // giao diện chọn phương thức thanh toán
    }

    // ===================== THANH TOÁN COD =====================
    @GetMapping("/cod")
    public String processCOD(HttpSession session, Model model) {

        Cart cart = (Cart) session.getAttribute("cart");
        Account user = (Account) session.getAttribute("account");

        if (cart == null || cart.getItems().isEmpty()) {
            model.addAttribute("error", "Giỏ hàng trống, không thể đặt COD.");
            return "error";
        }

        if (user == null) {
            model.addAttribute("error", "Bạn chưa đăng nhập.");
            return "error";
        }

        // Lấy trạng thái mặc định: PENDING
        Optional<OrderStatus> status = orderStatusRepository.findByName("PENDING");

        if (status.isEmpty()) {
            throw new RuntimeException("Không tìm thấy trạng thái PENDING trong DB!");
        }

        // Tạo Order
        Order order = new Order();
        order.setAccount(user);
        order.setOrderStatus(status.get());
        order.setOrderDate(LocalDateTime.now());
        order.setPaymentMethod("COD");
        order.setShippingAddress(user.getAccountDetail().getAddress());
        order.setOrderDetails(new ArrayList<>());

        // Tổng tiền
        BigDecimal total = BigDecimal.valueOf(cart.getTotalAmount());
        order.setTotalAmount(total);

        // Lưu đơn hàng
        Order savedOrder = orderRepository.save(order);

        // Lưu OrderDetail
        for (CartItem item : cart.getItems()) {
            OrderDetail detail = new OrderDetail();
            detail.setOrder(savedOrder);

            detail.setProductId(item.getProductId());
            detail.setProductName(item.getProductName());
            detail.setPrice(BigDecimal.valueOf(item.getPrice()));
            detail.setQuantity(item.getQuantity());

            orderDetailRepository.save(detail);
        }


        // Reset cart
        session.setAttribute("cart", new Cart());

        // Truyền orderId ra UI
        model.addAttribute("orderId", savedOrder.getId());
        model.addAttribute("totalAmount", total);

        return "checkout-cod-success";
    }



    // ===================== THANH TOÁN QUA VNPAY =====================
    @GetMapping("/vnpay")
    public String checkoutVnpayPage() {
        return "checkout-vnpay";
    }
}
