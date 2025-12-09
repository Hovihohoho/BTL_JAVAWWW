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

        // ✅ ĐÃ SỬA: Tìm Product từ DB và gán vào OrderDetail
        for (CartItem item : cartItems) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm có ID: " + item.getProductId()));

            OrderDetail detail = new OrderDetail();
            detail.setOrder(savedOrder);
            detail.setProduct(product); // Quan trọng: Gán object Product, không gán ID lẻ
            detail.setPrice(BigDecimal.valueOf(item.getPrice()));
            detail.setQuantity(item.getQuantity());

            orderDetailRepository.save(detail);
        }

        return savedOrder;
    }
    public Order updateOrderStatus(Long orderId, String statusName) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng!"));

        OrderStatus status = orderStatusRepository.findByName(statusName)
                .orElseGet(() -> {
                    OrderStatus s = new OrderStatus();
                    s.setName(statusName);
                    s.setDescription("Tự tạo khi cập nhật trạng thái");
                    return orderStatusRepository.save(s);
                });

        order.setOrderStatus(status);

        return orderRepository.save(order);
    }

    public void deleteById(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new RuntimeException("Đơn hàng không tồn tại!");
        }

        // Xóa OrderDetail trước (tránh lỗi FK)
        orderDetailRepository.deleteByOrderId(id);

        // Xóa Order
        orderRepository.deleteById(id);
    }



    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public List<Order> getOrdersByUsername(String username) {
        return orderRepository.findByAccountUsername(username);
    }
}