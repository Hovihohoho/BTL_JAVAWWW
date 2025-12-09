package iuh.btl_n7_iuh.repositories;

import iuh.btl_n7_iuh.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByAccountUsername(String username);
    // Dùng cho Cách A: kiểm tra tồn tại đơn hàng
    boolean existsByAccountId(Long accountId);

    // load luôn details + product + account + status để show trong bảng
    @Query("""
           select distinct o from Order o
           left join fetch o.account a
           left join fetch a.accountDetail ad
           left join fetch o.orderStatus s
           left join fetch o.orderDetails od
           left join fetch od.product p
           """)
    List<Order> findAllWithDetails();
}
