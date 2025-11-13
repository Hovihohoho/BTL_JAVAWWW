package iuh.btl_n7_iuh.services;

import iuh.btl_n7_iuh.dto.CartItem;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.ArrayList;
import java.util.List;

@Service
    @SessionAttributes("cart")
    public class CartService {

        public List<CartItem> getCartItemsFromSession(HttpServletRequest request) {
            List<CartItem> cart = (List<CartItem>) request.getSession().getAttribute("cart");
            return cart != null ? cart : new ArrayList<>();
        }

        public void clearCartSession(HttpServletRequest request) {
            request.getSession().removeAttribute("cart");
        }
    }

