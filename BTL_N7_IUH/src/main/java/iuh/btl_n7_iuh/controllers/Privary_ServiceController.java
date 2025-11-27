package iuh.btl_n7_iuh.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Privary_ServiceController {

    // Điều khoản dịch vụ
    @GetMapping("/term_of_service")
    public String termOfService() {
        return "term_of_service"; // tên file trong /templates
    }

    // Chính sách bảo mật
    @GetMapping("/privary")
    public String privary() {
        return "privary"; // tên file privary.html trong /templates
    }
}
