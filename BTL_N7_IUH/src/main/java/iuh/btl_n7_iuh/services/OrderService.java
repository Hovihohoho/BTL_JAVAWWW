package iuh.btl_n7_iuh.services;

import iuh.btl_n7_iuh.dto.CartItem;
import iuh.btl_n7_iuh.entities.*;
import iuh.btl_n7_iuh.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

        // ✅ SỬA: Dùng "PENDING" để khớp với DataBTL2.sql
        String statusName = paymentMethod.equalsIgnoreCase("COD") ? "PENDING" : "Đã thanh toán";

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

        // ✅ ĐÃ SỬA: Tìm Product từ DB và gán vào OrderDetail (QUAN TRỌNG)
        for (CartItem item : cartItems) {
            // 1. Tìm sản phẩm thực tế từ DB
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm ID: " + item.getProductId()));

            OrderDetail detail = new OrderDetail();
            detail.setOrder(savedOrder);

            // 2. Gán object Product (Hibernate sẽ tự lấy ID để lưu vào cột product_id)
            detail.setProduct(product);

            // 3. Các thông tin khác
            detail.setPrice(BigDecimal.valueOf(item.getPrice()));
            detail.setQuantity(item.getQuantity());

            orderDetailRepository.save(detail);
        }

        return savedOrder;
    }

    public List<Order> getOrdersByUsername(String username) {
        return orderRepository.findByAccountUsername(username);
    }

    // Các phương thức bổ sung
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    // Dùng cho Admin Controller (nếu có)
    public List<Order> findAllWithDetails() {
        return orderRepository.findAllWithDetails();
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public void updateOrderStatus(Long orderId, Long statusId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy order id = " + orderId));

        OrderStatus status = orderStatusRepository.findById(statusId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy trạng thái id = " + statusId));

        order.setOrderStatus(status);
        orderRepository.save(order);
    }

    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }
}