package iuh.btl_n7_iuh.controllers;



import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

@Controller
public class PaymentController {

    @GetMapping("/success")
    public String success() {
        return "checkout-success";
    }

    @GetMapping("/payment_return")
    public String paymentReturn(@RequestParam Map<String, String> params, Model model) {

        // Lấy dữ liệu từ VNPay
        String status = params.get("vnp_ResponseCode");
        String orderId = params.get("vnp_TxnRef");
        String amountRaw = params.get("vnp_Amount");
        String payDateRaw = params.get("vnp_PayDate");

        // === FORMAT SỐ TIỀN ===
        String formattedAmount = "";
        if (amountRaw != null) {

            // VNPay trả amount * 100 → cần chia lại 100
            long amount = Long.parseLong(amountRaw) / 100;

            NumberFormat nf = NumberFormat.getInstance(new Locale("vi", "VN"));
            formattedAmount = nf.format(amount) + " đ";
        }




        // === FORMAT NGÀY THANH TOÁN ===
        String formattedPayDate = "";
        if (payDateRaw != null && payDateRaw.length() == 14) {
            String yyyy = payDateRaw.substring(0, 4);
            String mm = payDateRaw.substring(4, 6);
            String dd = payDateRaw.substring(6, 8);
            String hh = payDateRaw.substring(8, 10);
            String mi = payDateRaw.substring(10, 12);
            String ss = payDateRaw.substring(12, 14);

            formattedPayDate = dd + "/" + mm + "/" + yyyy + " " + hh + ":" + mi + ":" + ss;
        }

        // === ĐƯA VÀO MODEL ===
        model.addAttribute("status", status);
        model.addAttribute("orderId", orderId);
        model.addAttribute("formattedAmount", formattedAmount);
        model.addAttribute("formattedPayDate", formattedPayDate);

        return "payment_return";
    }

}



