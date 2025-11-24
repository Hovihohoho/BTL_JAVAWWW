package iuh.btl_n7_iuh.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CartItem {
    private Long productId;
    private String productName;
    private double price;
    private int quantity;
}
