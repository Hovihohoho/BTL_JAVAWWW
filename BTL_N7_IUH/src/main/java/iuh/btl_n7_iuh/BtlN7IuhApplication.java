package iuh.btl_n7_iuh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry  // QUAN TRỌNG: THÊM DÒNG NÀY
public class BtlN7IuhApplication {
    public static void main(String[] args) {
        SpringApplication.run(BtlN7IuhApplication.class, args);
    }
}