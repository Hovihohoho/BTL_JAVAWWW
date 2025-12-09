package iuh.btl_n7_iuh.controllers;

import iuh.btl_n7_iuh.services.GeminiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final GeminiService geminiService;

    // Nếu dùng cùng domain với frontend (thymeleaf) thì không cần @CrossOrigin
    @PostMapping("/ask")
    public ResponseEntity<Map<String, String>> ask(@RequestBody Map<String, String> payload) {
        try {
            String message = payload.get("message");
            if (message == null || message.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("reply", "Bạn chưa nhập câu hỏi."));
            }

            // Gọi service Gemini (có retry)
            String reply = geminiService.callGemini(message);

            if (reply == null || reply.isBlank()) {
                // fallback message
                reply = "Xin lỗi, trợ lý đang bận. Vui lòng thử lại sau.";
            }

            return ResponseEntity.ok(Map.of("reply", reply));

        } catch (Exception ex) {
            // Log rõ ràng để bạn kiểm tra server logs
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("reply", "Có lỗi phía server: " + ex.getMessage()));
        }
    }
}
