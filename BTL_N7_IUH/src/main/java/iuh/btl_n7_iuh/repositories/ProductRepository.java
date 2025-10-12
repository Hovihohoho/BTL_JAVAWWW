package iuh.btl_n7_iuh.repositories;

import iuh.btl_n7_iuh.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Phương thức tự động: Lấy các sản phẩm có giá khuyến mãi
    List<Product> findBySalePriceIsNotNull();
}