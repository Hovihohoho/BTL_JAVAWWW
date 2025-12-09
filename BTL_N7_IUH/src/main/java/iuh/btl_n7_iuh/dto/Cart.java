package iuh.btl_n7_iuh.dto;

import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cart {

    private List<CartItem> items = new ArrayList<>();

    public void addItem(CartItem item) {
        for (CartItem existing : items) {
            if (existing.getProductId().equals(item.getProductId())) {
                existing.setQuantity(existing.getQuantity() + item.getQuantity());
                return;
            }
        }
        items.add(item);
    }

    public void removeItem(Long productId) {
        items.removeIf(i -> i.getProductId().equals(productId));
    }

    public double getTotalAmount() {
        double total = 0;
        for (CartItem item : items) {
            total += item.getPrice() * item.getQuantity();
        }
        return total;
    }

    public void clear() {
        items.clear();
    }
}
