// Sửa đổi file hovihohoho/btl_javawww/BTL_JAVAWWW-4f085c4d7320c6d49552c2057a7f23ab693c7ce7/BTL_N7_IUH/src/main/java/iuh/btl_n7_iuh/controllers/CheckoutController.java (Hoặc tạo file mới nếu bạn muốn giữ lại)
package iuh.btl_n7_iuh.controllers;

import iuh.btl_n7_iuh.dto.CartItem; // Import CartItem
import iuh.btl_n7_iuh.services.CartService; // Import CartService
import iuh.btl_n7_iuh.services.OrderService; // Import OrderService
import jakarta.servlet.http.HttpServletRequest; // Import HttpServletRequest
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal; // Import Auth Principal
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/checkout")
@RequiredArgsConstructor
public class CheckoutController {

    private final OrderService orderService; // Sử dụng OrderService
    private final CartService cartService; // Sử dụng CartService

    // Lưu ý: Cần thêm logic lưu CartItems từ localStorage vào HttpSession 
    // TRƯỚC KHI gọi hàm này, hoặc sửa CartService để đọc localStorage nếu 
    // không dùng được session (như VnpayController đã làm)

    // ===================== THANH TOÁN COD ĐÃ SỬA =====================
    // Endpoint này được gọi từ cart.html bằng JS: window.location.href = `/checkout/cod?amount=${grandTotal}`;
    @GetMapping("/cod")
    public String processCOD(@RequestParam("amount") Long amount,
                             HttpServletRequest request,
                             Model model,
                             @AuthenticationPrincipal String username) { // Lấy username đang đăng nhập

        // Lấy thông tin cần thiết từ Request/Session
        String userUsername = request.getUserPrincipal().getName(); // Lấy username từ Spring Security

        // 1. Lấy Cart Items từ Session (dự án này có vẻ dùng Session cho Cart khi chuyển từ JS lên)
        List<CartItem> cartItems = cartService.getCartItemsFromSession(request);

        // **GIẢ ĐỊNH** các trường Name, Address, Phone được lưu/lấy ở đâu đó hoặc từ AccountDetail
        // Trong luồng COD, ta cần lấy dữ liệu này từ form trong cart.html
        // Vì cart.html không gửi đủ data, ta cần fix nó hoặc lấy từ AccountDetail (chưa có trong luồng này)
        String address = "Địa chỉ mặc định/cần phải lấy từ form...";

        if (cartItems.isEmpty() || address.contains("mặc định/cần phải lấy")) {
            // Thêm logic hiển thị lỗi và yêu cầu nhập địa chỉ nếu cần
            // Nếu không có sản phẩm trong Session (vì front-end dùng localStorage), sẽ bị lỗi
            model.addAttribute("errorMessage", "Không tìm thấy sản phẩm trong giỏ hàng hoặc thiếu thông tin nhận hàng.");
            return "checkout-fail"; // Trả về trang lỗi
        }

        try {
            // 2. LƯU ĐƠN HÀNG VÀO DATABASE thông qua OrderService
            orderService.createOrder(
                    userUsername,
                    BigDecimal.valueOf(amount),
                    "COD",
                    address, // Cần lấy địa chỉ thực tế từ request
                    cartItems
            );

            // 3. XÓA GIỎ HÀNG (trong Session)
            cartService.clearCartSession(request);

            // 4. Trả về trang thông báo thành công
            return "checkout-cod"; // -> templates/checkout-cod.html

        } catch (Exception e) {
            System.err.println("Lỗi tạo đơn hàng COD: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("errorMessage", "Lỗi xử lý đơn hàng: " + e.getMessage());
            return "checkout-fail";
        }
    }

    @GetMapping("/online")
    public String checkoutOnline() {
        return "checkout-online";
    }

    @GetMapping("/vnpay")
    public String payVnpay() {
        return "checkout-vnpay";
    }

    @GetMapping("/momo")
    public String payMomo() {
        return "checkout-momo";
    }

    // Các endpoint khác
    @GetMapping("/success")
    public String success() {
        return "checkout-success";
    }

    @GetMapping("/checkout/cod")
    public String codCheckoutOld(@RequestParam("amount") Long amount, Model model) {
        // Endpoint cũ, không nên dùng
        model.addAttribute("amount", amount);
        return "checkout-success";
    }

    @GetMapping("/checkout/online")
    public String checkoutOnlineOld(@RequestParam("amount") Long amount, Model model) {
        // Endpoint cũ, không nên dùng
        model.addAttribute("amount", amount);
        return "checkout-online";
    }
}