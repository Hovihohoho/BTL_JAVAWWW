package iuh.btl_n7_iuh.controllers.admin;

import iuh.btl_n7_iuh.entities.Category;
import iuh.btl_n7_iuh.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {

    private final CategoryRepository categoryRepository;

    @GetMapping("")
    public String listCategories(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("title", "Quản lý Danh mục - Frubana");
        return "admin/categories/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("category", new Category());
        model.addAttribute("title", "Thêm Danh mục mới");
        return "admin/categories/add-edit"; // Sử dụng template chung
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        model.addAttribute("category", category);
        model.addAttribute("title", "Sửa Danh mục: " + category.getName());
        return "admin/categories/add-edit";
    }

    @PostMapping("/save")
    public String saveCategory(@ModelAttribute Category category, RedirectAttributes redirectAttributes) {
        try {
            if (category.getSlug() == null || category.getSlug().isEmpty()) {
                // Tự động tạo slug đơn giản
                category.setSlug(category.getName().toLowerCase().replaceAll("\\s+", "-"));
            }
            categoryRepository.save(category);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Lưu danh mục thành công: " + category.getName());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Lỗi khi lưu danh mục. Tên hoặc Slug có thể đã tồn tại.");
        }
        return "redirect:/admin/categories";
    }

    @PostMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            categoryRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Xóa danh mục thành công.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Không thể xóa danh mục này do có sản phẩm liên quan.");
        }
        return "redirect:/admin/categories";
    }
}