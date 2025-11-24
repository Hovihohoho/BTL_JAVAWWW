package iuh.btl_n7_iuh.controllers.admin;

import iuh.btl_n7_iuh.entities.Comment;
import iuh.btl_n7_iuh.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/comments")
public class AdminCommentController {

    private final CommentService commentService;

    // 📌 Hiển thị danh sách bình luận
    @GetMapping
    public String listComments(Model model) {
        model.addAttribute("comments", commentService.getAllComments());
        return "admin/comments/list";
    }

    // 📌 Xóa bình luận
    @PostMapping("/delete/{id}")
    public String deleteComment(@PathVariable Long id, RedirectAttributes redirectAttributes) {

        boolean deleted = commentService.deleteComment(id);

        if (deleted) {
            redirectAttributes.addFlashAttribute("successMessage", "Xóa bình luận thành công!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy bình luận để xóa!");
        }

        return "redirect:/admin/comments";
    }
}
