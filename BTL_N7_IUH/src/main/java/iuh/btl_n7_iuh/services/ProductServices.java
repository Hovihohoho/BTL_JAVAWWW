package iuh.btl_n7_iuh.services;

import iuh.btl_n7_iuh.entities.Product;
import iuh.btl_n7_iuh.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServices {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getFeaturedProducts() {
        return productRepository.findTop6ByOrderByPriceDesc();
    }

    public void getBestSellers() {
    }
}
