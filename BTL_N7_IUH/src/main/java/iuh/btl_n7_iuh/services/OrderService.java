package iuh.btl_n7_iuh.services;
import iuh.btl_n7_iuh.dto.CartItem;
import iuh.btl_n7_iuh.entities.*;
import iuh.btl_n7_iuh.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private OrderStatusRepository orderStatusRepository;

    // ✅ Tạo đơn hàng mới
    public Order createOrder(String username, BigDecimal totalAmount, String paymentMethod,
                             String address, List<CartItem> cartItems) {

        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản!"));

        // Lấy trạng thái phù hợp
        String statusName = paymentMethod.equalsIgnoreCase("COD") ? "Đang xử lý" : "Đã thanh toán";

        OrderStatus status = orderStatusRepository.findByName(statusName)
                .orElseGet(() -> {
                    OrderStatus s = new OrderStatus();
                    s.setName(statusName);
                    s.setDescription("Tự tạo khi thanh toán");
                    return orderStatusRepository.save(s);
                });

        Order order = new Order();
        order.setAccount(account);
        order.setOrderStatus(status);
        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(totalAmount);
        order.setShippingAddress(address);
        order.setPaymentMethod(paymentMethod);

        Order savedOrder = orderRepository.save(order);

        // Lưu chi tiết đơn hàng
        for (CartItem item : cartItems) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

            OrderDetail detail = new OrderDetail();
            detail.setOrder(savedOrder);
            detail.setProduct(product);
            detail.setQuantity(item.getQuantity());
            detail.setPrice(BigDecimal.valueOf(item.getPrice()));

            orderDetailRepository.save(detail);
        }

        return savedOrder;
    }

    public List<Order> getOrdersByUsername(String username) {
        return orderRepository.findByAccountUsername(username);
    }
}
