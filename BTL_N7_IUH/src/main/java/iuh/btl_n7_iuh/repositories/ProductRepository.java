package iuh.btl_n7_iuh.repositories;

import iuh.btl_n7_iuh.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Lấy 6 sản phẩm mới nhất (theo giá giảm dần)
    List<Product> findTop6ByOrderByPriceDesc();

    // Tìm sản phẩm theo tên (không phân biệt hoa thường)
    List<Product> findByNameContainingIgnoreCase(String keyword);

    // Lọc sản phẩm theo slug của Category
    List<Product> findByCategory_Slug(String slug);
}
