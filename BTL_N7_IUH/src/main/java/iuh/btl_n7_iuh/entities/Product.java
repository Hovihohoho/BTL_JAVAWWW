package iuh.btl_n7_iuh.entities;



import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "Products")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private BigDecimal price;
    private BigDecimal salePrice;
    private String imageUrl;
    @Lob
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    // TODO: Bổ sung Constructor, Getters và Setters
    // (Bao gồm Product(), getId(), getName(), getPrice(), getImageUrl(), ...)
}