package iuh.btl_n7_iuh.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Banners")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Banner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    private String title;
    private String linkUrl;

    @Column(name = "is_active")
    private Boolean isActive = true;
}
