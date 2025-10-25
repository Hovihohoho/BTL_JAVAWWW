package iuh.btl_n7_iuh.services;

import iuh.btl_n7_iuh.entities.Product;
import iuh.btl_n7_iuh.repositories.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public List<Product> getFeaturedProducts() {
        return productRepository.findTop6ByOrderByPriceDesc();
    }

    public Product getById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public List<Product> search(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }

    public List<Product> filterBySlug(String slug) {
        return productRepository.findByCategory_Slug(slug);
    }
}

