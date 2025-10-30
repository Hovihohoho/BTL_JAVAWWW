package iuh.btl_n7_iuh.controllers;

import iuh.btl_n7_iuh.services.ProductService;
import iuh.btl_n7_iuh.services.CategoryService;
import iuh.btl_n7_iuh.entities.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;


    // ✅ Map slug URL → slug trong DB
    private final Map<String, String> slugMap = Map.of(
            "fruit", "trai-cay",
            "vegetable", "rau-huu-co"
    );

    @ModelAttribute("categories")
    public Object loadCategories() {
        return categoryService.getAll();
    }

    // ✅ Xem tất cả sản phẩm
    @GetMapping("/all")
    public String all(Model model) {
        model.addAttribute("products", productService.getAll());
        model.addAttribute("activeMenu", "products");
        return "product";
    }

    // ✅ Xem chi tiết sản phẩm
    @GetMapping("/detail/{id}")
    public String productDetail(@PathVariable Long id, Model model) {
        Product product = productService.getById(id);
        if (product == null) return "redirect:/products/all";
        model.addAttribute("product", product);
        model.addAttribute("activeMenu", "products");

        return "product-detail";
    }

    // ✅ Tìm kiếm sản phẩm
    @GetMapping("/search")
    public String search(@RequestParam("keyword") String keyword, Model model) {
        model.addAttribute("products", productService.search(keyword));
        model.addAttribute("activeMenu", "products");

        return "product";
    }

    // ✅ Lọc sản phẩm theo slug
    @GetMapping("/{slug}")
    public String filterBySlug(@PathVariable String slug, Model model) {

        String realSlug = slugMap.get(slug);

        if (realSlug == null) {
            model.addAttribute("products", productService.getAll());
        } else {
            model.addAttribute("products", productService.filterBySlug(realSlug));
        }

        model.addAttribute("activeMenu", "products");

        return "product";
    }
}
