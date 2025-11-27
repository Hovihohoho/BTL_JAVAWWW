package iuh.btl_n7_iuh.repositories;

import iuh.btl_n7_iuh.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByAccountUsername(String username);
    // Dùng cho Cách A: kiểm tra tồn tại đơn hàng
    boolean existsByAccountId(Long accountId);
}
