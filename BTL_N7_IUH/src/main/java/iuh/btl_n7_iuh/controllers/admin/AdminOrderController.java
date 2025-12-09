package iuh.btl_n7_iuh.controllers.admin;

import iuh.btl_n7_iuh.entities.Order;
import iuh.btl_n7_iuh.entities.OrderDetail;
import iuh.btl_n7_iuh.entities.OrderStatus;
import iuh.btl_n7_iuh.services.OrderDetailService;
import iuh.btl_n7_iuh.services.OrderService;
import iuh.btl_n7_iuh.services.OrderStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderService orderService;
    private final OrderStatusService orderStatusService;
    private final OrderDetailService orderDetailService;

    // ================== LIST ==================
    @GetMapping
    public String listOrders(Model model) {

        // ✅ Lấy TẤT CẢ đơn hàng
        List<Order> orders = orderService.findAll();

        // ✅ Lấy tất cả trạng thái
        List<OrderStatus> statuses = orderStatusService.findAll();

        // ✅ Map <orderId, List<OrderDetail>> để hiển thị dưới từng đơn
        Map<Long, List<OrderDetail>> orderDetailsMap = new HashMap<>();
        for (Order order : orders) {
            List<OrderDetail> details =
                    orderDetailService.findByOrderId(order.getId()); // DÙNG SERVICE, KHÔNG DÙNG order.getOrderDetails()
            orderDetailsMap.put(order.getId(), details);
        }

        model.addAttribute("orders", orders);
        model.addAttribute("statuses", statuses);
        model.addAttribute("orderDetailsMap", orderDetailsMap);

        return "admin/orders/list";
    }

    // ================== UPDATE STATUS ==================
    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable("id") Long orderId,
                               @RequestParam("statusId") Long statusId,
                               RedirectAttributes redirectAttributes) {
        try {
            orderService.updateOrderStatus(orderId, statusId);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật trạng thái thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi cập nhật trạng thái: " + e.getMessage());
        }
        return "redirect:/admin/orders";
    }

    // ================== DELETE ORDER ==================
    @PostMapping("/delete/{id}")
    public String deleteOrder(@PathVariable("id") Long orderId,
                              RedirectAttributes redirectAttributes) {
        try {
            orderService.deleteById(orderId);
            redirectAttributes.addFlashAttribute("successMessage", "Xóa đơn hàng thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi xóa đơn hàng: " + e.getMessage());
        }
        return "redirect:/admin/orders";
    }
}
