package iuh.btl_n7_iuh.controllers;

import iuh.btl_n7_iuh.repositories.TestimonialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestimonialController {
    private final TestimonialRepository repo;
    public TestimonialController(TestimonialRepository repo) { this.repo = repo; }

    // Cho phép cả 2 đường dẫn
    @GetMapping({"/binh-luan-khach-hang", "/testimonials"})
    public String testimonials(Model model) {
        model.addAttribute("testimonials",
                repo.findLatest(org.springframework.data.domain.PageRequest.of(0, 12)));
        return "testimonials"; // KHÔNG thêm .html, không có dấu /
    }
}

