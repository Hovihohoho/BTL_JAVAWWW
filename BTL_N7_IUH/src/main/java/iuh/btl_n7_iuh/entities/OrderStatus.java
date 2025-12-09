package iuh.btl_n7_iuh.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_statuses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "status_name", nullable = false, unique = true, length = 50)
    private String name;

    // ✅ Thêm cột mô tả để tránh lỗi setDescription()
    @Column(length = 255)
    private String description;
}
