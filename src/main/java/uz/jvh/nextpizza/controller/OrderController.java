package uz.jvh.nextpizza.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import uz.jvh.nextpizza.dto.request.order.UpdateOrderStatusRequest;
import uz.jvh.nextpizza.dto.request.order.CreateOrderRequest;
import uz.jvh.nextpizza.dto.response.order.OrderResponse;
import uz.jvh.nextpizza.enomerator.OrderStatus;
import uz.jvh.nextpizza.entity.User;
import uz.jvh.nextpizza.service.OrderService;

import java.util.List;

@RequestMapping("/api/order")
@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * Buyurtma berish (Cart → Order)
     * POST /api/orders
     */
    @PostMapping("create-orders")
    public ResponseEntity<OrderResponse> createOrder(@AuthenticationPrincipal User user,
                                                     @Valid @RequestBody CreateOrderRequest request) {

        OrderResponse response = orderService.createOrder(user.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Mening buyurtmalarim
     * GET /api/orders/my-orders
     */
    @GetMapping("/my-orders")
    public ResponseEntity<List<OrderResponse>> getMyOrders(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(orderService.getMyOrders(user.getId()));
    }

    /**
     * Buyurtma detallari
     * GET /api/orders/{orderId}
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(@AuthenticationPrincipal User user, @PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderById(user.getId(), orderId));
    }

    /**
     * Buyurtmani bekor qilish
     * PUT /api/orders/{orderId}/cancel
     */
    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(@AuthenticationPrincipal User user, @PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.cancelOrder(user.getId(), orderId));
    }


    // ========== ADMIN ENDPOINTS ==========
    /**
     * Barcha buyurtmalar (Admin)
     * GET /api/orders/admin/all
     */
    @GetMapping("/admin/all")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('OWNER')")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    /**
     * Status bo'yicha buyurtmalar (Admin)
     * GET /api/orders/admin/status/{status}
     */
    @GetMapping("/admin/status/{status}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('OWNER')")
    public ResponseEntity<List<OrderResponse>> getOrdersByStatus(@PathVariable OrderStatus status) {

        return ResponseEntity.ok(orderService.getOrdersByStatus(status));
    }

    /**
     * Buyurtma statusini o'zgartirish (Admin)
     * PUT /api/orders/admin/{orderId}/status
     */
    @PutMapping("/admin/{orderId}/status")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('OWNER')")
    public ResponseEntity<OrderResponse> updateOrderStatus(@PathVariable Long orderId,
                                                           @Valid @RequestBody UpdateOrderStatusRequest request) {

        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, request.getStatus()));
    }

    @GetMapping("/get-order-counts")
    @PreAuthorize("hasAnyAuthority('ADMIN') or hasAnyAuthority('OWNER')")
    public ResponseEntity<Long> getOrderCounts() {
        return ResponseEntity.ok(orderService.allOrdersCount());
    }

    @GetMapping("/get-today's-order-counts")
    @PreAuthorize("hasAnyAuthority('ADMIN') or hasAnyAuthority('OWNER')")
    public ResponseEntity<Long> getTodayOrderCounts() {
        return ResponseEntity.ok(orderService.getTodayOrdersCount());
    }

}
