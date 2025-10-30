package iuh.btl_n7_iuh.repositories;

import iuh.btl_n7_iuh.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findTop6ByOrderByPriceDesc(); // lấy 6 sản phẩm mới nhất
<<<<<<< HEAD

    List<Product> findByNameContainingIgnoreCase(String keyword);

    // Lọc theo slug của Category
    List<Product> findByCategory_Slug(String slug);
=======
>>>>>>> 2ae1df9f312eac7306823a73ceed800f5fac9dfd
}
