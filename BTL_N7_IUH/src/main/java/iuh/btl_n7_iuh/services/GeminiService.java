package iuh.btl_n7_iuh.services;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GeminiService { // Bạn có thể giữ nguyên tên class để đỡ phải sửa Controller

    // 👇 DÁN KEY GROQ CỦA BẠN VÀO ĐÂY (Bắt đầu bằng gsk_...)
    // Đã xóa phần lặp lại thừa ở đuôi
    private static final String API_KEY = "";

    // URL của Groq AI
    private static final String API_URL = "https://api.groq.com/openai/v1/chat/completions";

    private static final String SYSTEM_PROMPT = """
            Bạn là Trợ lý ảo AI của cửa hàng thực phẩm Frubana.
            Phong cách: Thân thiện, nhiệt tình, dùng nhiều emoji 🍎🥦.
            Nhiệm vụ: Tư vấn bán hàng trái cây, rau củ.
            
            Thông tin cửa hàng:
            - Phí ship: 15k nội thành, Freeship đơn > 300k.
            - Địa chỉ: 12 Nguyễn Văn Bảo, Gò Vấp, TP.HCM.
            - Sản phẩm HOT: Dâu tây (65k), Nho Mỹ (120k), Táo Envy (80k).
            - Chính sách: Bao ăn, 1 đổi 1 trong 24h.
            
            Hãy trả lời ngắn gọn (dưới 3 câu) và luôn hướng khách chốt đơn.
            """;

    @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public String callGemini(String userMessage) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + API_KEY); // Groq dùng Bearer Token

            // Cấu trúc JSON của Groq (Chuẩn OpenAI)
            Map<String, Object> requestBody = new HashMap<>();
            // Dùng bản Llama 3.3 mới nhất của Groq
            requestBody.put("model", "llama-3.3-70b-versatile");

            // Tin nhắn: Gồm System Prompt và User Message
            Map<String, String> systemMsg = Map.of("role", "system", "content", SYSTEM_PROMPT);
            Map<String, String> userMsg = Map.of("role", "user", "content", userMessage);

            requestBody.put("messages", List.of(systemMsg, userMsg));
            requestBody.put("temperature", 0.7); // Độ sáng tạo

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            // Gửi Request
            ResponseEntity<String> response = restTemplate.postForEntity(API_URL, entity, String.class);

            // Đọc kết quả JSON trả về
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());

            // Đường dẫn lấy câu trả lời: choices[0].message.content
            return root.path("choices").get(0)
                    .path("message")
                    .path("content").asText();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi gọi Groq API: " + e.getMessage());
        }
    }

    @Recover
    public String recover(RuntimeException e, String userMessage) {
        return "Server đang bảo trì một chút, bạn quay lại sau 1 phút nhé! 😓";
    }
}