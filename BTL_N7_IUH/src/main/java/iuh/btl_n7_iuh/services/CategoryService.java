package iuh.btl_n7_iuh.services;

import iuh.btl_n7_iuh.entities.Category;
import iuh.btl_n7_iuh.repositories.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    public Category getBySlug(String slug) {
        return categoryRepository.findBySlug(slug);
    }
}
