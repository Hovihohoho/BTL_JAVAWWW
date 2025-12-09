package iuh.btl_n7_iuh.controllers.admin;

import iuh.btl_n7_iuh.entities.Category;
import iuh.btl_n7_iuh.entities.Product;
import iuh.btl_n7_iuh.repositories.CategoryRepository;
import iuh.btl_n7_iuh.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/admin/products")
@RequiredArgsConstructor
public class AdminProductController {

    // Đường dẫn lưu file TỪ GỐC DỰ ÁN (cần kiểm tra lại nếu chạy trên IDE)
    public static String UPLOAD_DIR = "src/main/resources/static/images/";

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    // Tự động tải danh mục cho dropdown trong form
    @ModelAttribute("categories")
    public List<Category> populateCategories() {
        return categoryRepository.findAll();
    }

    @GetMapping("")
    public String listProducts(Model model) {
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("title", "Quản lý Sản phẩm - Frubana");
        return "admin/products/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("title", "Thêm Sản phẩm mới");
        return "admin/products/add-edit";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        model.addAttribute("product", product);
        model.addAttribute("title", "Sửa Sản phẩm: " + product.getName());
        return "admin/products/add-edit";
    }

    @PostMapping("/save")
    public String saveProduct(@ModelAttribute Product product,
                              @RequestParam("category_id") Long categoryId,
                              @RequestParam("imageFile") MultipartFile imageFile,
                              RedirectAttributes redirectAttributes) {
        try {
            // 1. Handle Category
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(category);

            // 2. Handle Image Upload (chỉ xử lý khi có file mới)
            if (!imageFile.isEmpty()) {
                String originalFilename = imageFile.getOriginalFilename();
                String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
                // Tạo tên file mới để tránh trùng lặp
                String fileName = product.getName().replaceAll("[^a-zA-Z0-9.-]", "_") + "_" + System.currentTimeMillis() + fileExtension;
                Path path = Paths.get(UPLOAD_DIR + fileName);
                Files.write(path, imageFile.getBytes());
                product.setImageUrl(fileName); // Cập nhật URL ảnh mới
            } else if (product.getId() == null) {
                // Yêu cầu ảnh nếu là sản phẩm mới
                redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng chọn ảnh cho sản phẩm mới.");
                return "redirect:/admin/products/add";
            }
            // Nếu edit và không upload file mới, imageUrl cũ (được truyền qua hidden field) sẽ được giữ lại.

            // Cleanup: nếu salePrice là 0 thì lưu là null
            if (product.getSalePrice() != null && product.getSalePrice().compareTo(BigDecimal.ZERO) == 0) {
                product.setSalePrice(null);
            }

            // 3. Save Product
            productRepository.save(product);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Lưu sản phẩm thành công: " + product.getName());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Lỗi khi lưu sản phẩm: " + e.getMessage());
        }
        return "redirect:/admin/products";
    }

    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            productRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Xóa sản phẩm thành công.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Không thể xóa sản phẩm này.");
        }
        return "redirect:/admin/products";
    }
}