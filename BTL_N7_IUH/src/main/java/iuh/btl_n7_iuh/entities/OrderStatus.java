package iuh.btl_n7_iuh.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Order_Statuses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ Sửa tên thành "name" cho khớp với OrderService
    @Column(nullable = false, unique = true, length = 50)
    private String name;

    // ✅ Thêm cột mô tả để tránh lỗi setDescription()
    @Column(length = 255)
    private String description;
}
