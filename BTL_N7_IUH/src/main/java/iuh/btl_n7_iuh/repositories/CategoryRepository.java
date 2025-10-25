package iuh.btl_n7_iuh.repositories;

import iuh.btl_n7_iuh.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findBySlug(String slug);
}
