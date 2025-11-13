package iuh.btl_n7_iuh.controllers;

import iuh.btl_n7_iuh.dto.CartItem;
import iuh.btl_n7_iuh.services.CartService;
import iuh.btl_n7_iuh.services.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class VnpayController {

    @Value("${vnpay.tmnCode}")
    private String vnp_TmnCode;

    @Value("${vnpay.hashSecret}")
    private String vnp_HashSecret;

    @Value("${vnpay.payUrl}")
    private String vnp_PayUrl;

    @Value("${vnpay.returnUrl}")
    private String vnp_ReturnUrl;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    // ==========================
    // 1️⃣ Tạo link thanh toán VNPay
    // ==========================
    @GetMapping("/payment/vnpay")
    public String payment(@RequestParam("amount") long amount, HttpServletRequest req) {

        String orderId = UUID.randomUUID().toString().substring(0, 8);
        long vnpAmount = amount * 100; // VNPay yêu cầu nhân 100

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", "2.1.0");
        vnp_Params.put("vnp_Command", "pay");
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(vnpAmount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", orderId);
        vnp_Params.put("vnp_OrderInfo", "Thanh toán đơn hàng #" + orderId);
        vnp_Params.put("vnp_OrderType", "other");
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", req.getRemoteAddr());

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        vnp_Params.put("vnp_CreateDate", formatter.format(cld.getTime()));

        // Build hash & query
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        for (Iterator<String> itr = fieldNames.iterator(); itr.hasNext();) {
            String fieldName = itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                hashData.append(fieldName).append('=')
                        .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII))
                        .append('=')
                        .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                if (itr.hasNext()) {
                    hashData.append('&');
                    query.append('&');
                }
            }
        }

        String vnp_SecureHash = HmacUtils.hmacSha512Hex(vnp_HashSecret, hashData.toString());
        query.append("&vnp_SecureHash=").append(vnp_SecureHash);

        String paymentUrl = vnp_PayUrl + "?" + query;

        System.out.println("VNPay URL: " + paymentUrl);
        System.out.println("Gửi số tiền thực tế: " + amount + " VND");

        return "redirect:" + paymentUrl;
    }

    // ==========================
    // 2️⃣ Xử lý sau khi thanh toán
    // ==========================
    @GetMapping("/payment/vnpay_return")
    public String vnpayReturn(@RequestParam Map<String, String> allParams,
                              HttpServletRequest request) {
        System.out.println("VNPay return params: " + allParams);

        String vnp_ResponseCode = allParams.get("vnp_ResponseCode");

        if ("00".equals(vnp_ResponseCode)) {
            try {
                String username = request.getUserPrincipal().getName(); // người dùng hiện tại
                long amount = Long.parseLong(allParams.get("vnp_Amount")) / 100; // chia lại 100
                String address = "Địa chỉ mặc định";

                List<CartItem> cartItems = cartService.getCartItemsFromSession(request);

                orderService.createOrder(
                        username,
                        BigDecimal.valueOf(amount),
                        "VNPay",
                        address,
                        cartItems
                );

                cartService.clearCartSession(request); // xóa giỏ sau khi lưu đơn

                return "redirect:/orders";
            } catch (Exception e) {
                e.printStackTrace();
                return "redirect:/checkout-fail";
            }
        } else {
            return "redirect:/checkout-fail";
        }
    }
}
