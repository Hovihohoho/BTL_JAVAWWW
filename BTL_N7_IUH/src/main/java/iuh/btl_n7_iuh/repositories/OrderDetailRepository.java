package iuh.btl_n7_iuh.repositories;

import iuh.btl_n7_iuh.entities.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    List<OrderDetail> findByOrderId(Long orderId);

    void deleteByOrderId(Long orderId);

}
