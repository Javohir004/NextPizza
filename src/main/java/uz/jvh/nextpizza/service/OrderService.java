package uz.jvh.nextpizza.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.jvh.nextpizza.dto.request.order.CreateOrderRequest;
import uz.jvh.nextpizza.dto.response.UserResponse;
import uz.jvh.nextpizza.dto.response.order.OrderItemResponse;
import uz.jvh.nextpizza.dto.response.order.OrderResponse;
import uz.jvh.nextpizza.enomerator.ErrorCode;
import uz.jvh.nextpizza.enomerator.OrderStatus;
import uz.jvh.nextpizza.entity.*;
import uz.jvh.nextpizza.exception.NextPizzaException;
import uz.jvh.nextpizza.repository.OrderRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final UserService userService;

    /**
     * Buyurtma berish (Cart → Order)
     */
    @Transactional
    public OrderResponse createOrder(Long userId, CreateOrderRequest request) {
        User user = userService.findByIdE(userId);
        Cart cart = cartService.getCart(userId);

        // Savat bo'sh bo'lsa - xato
        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new NextPizzaException(ErrorCode.CART_EMPTY);
        }

        // Balans tekshirish
        if (user.getBalance().compareTo(cart.getTotalPrice()) < 0) {
            throw new NextPizzaException(ErrorCode.INSUFFICIENT_BALANCE,
                    String.format("Kerak: %s, Mavjud: %s", cart.getTotalPrice(), user.getBalance()));
        }

        Order order = Order.builder().
                user(user)
                .orderDate(LocalDateTime.now())
                .orderStatus(OrderStatus.CREATED)
                .totalPrice(cart.getTotalPrice())
                .totalItems(cart.getTotalItems())
                .deliveryAddress(request.getDeliveryAddress())
                .orderItems(new ArrayList<>()).build();

        // Cart items → Order items
        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .pizza(cartItem.getPizza())
                    .drink(cartItem.getDrink())
                    .productName(getProductName(cartItem))
                    .quantity(cartItem.getQuantity())
                    .price(cartItem.getPrice())
                    .totalPrice(cartItem.getTotalPrice())
                    .build();
            order.getOrderItems().add(orderItem);
        }

        // Balansdan ayirish
        user.setBalance(user.getBalance().subtract(cart.getTotalPrice()));
        Order savedOrder = orderRepository.save(order);

        // Savatni tozalash
        cartService.clearCart(userId);
        return toOrderResponse(savedOrder);
    }

    /**
     * Mening buyurtmalarim
     */
    public List<OrderResponse> getMyOrders(Long userId) {
        User user = userService.findByIdE(userId);
        List<Order> orders = orderRepository.findByUserOrderByOrderDateDesc(user);
        return orders.stream().map(this::toOrderResponse).toList();
    }

    /**
     * Buyurtma detallari
     */
    public OrderResponse getOrderById(Long userId, Long orderId) {
        Order order = orderRepository.findById(orderId).
                orElseThrow(() -> new NextPizzaException(ErrorCode.ORDER_NOT_FOUND, "ID: " + orderId));

        // Faqat o'z buyurtmangizni ko'rish mumkin
        if (!order.getUser().getId().equals(userId)) {
            throw new NextPizzaException(ErrorCode.FORBIDDEN, "Bu buyurtma sizniki emas");
        }
        return toOrderResponse(order);
    }

    /**
     * Buyurtmani bekor qilish
     */
    @Transactional
    public OrderResponse cancelOrder(Long userId, Long orderId) {
        Order order = orderRepository.findById(orderId).
                orElseThrow(() -> new NextPizzaException(ErrorCode.ORDER_NOT_FOUND, "ID: " + orderId));

        // Faqat o'z buyurtmangizni bekor qilish mumkin
        if (!order.getUser().getId().equals(userId)) {
            throw new NextPizzaException(ErrorCode.FORBIDDEN, "Bu buyurtma sizniki emas");
        }

        // Faqat CREATED yoki COOKING holatida bekor qilish mumkin
        if (order.getOrderStatus() == OrderStatus.DELIVERING || order.getOrderStatus() == OrderStatus.COMPLETED) {
            throw new NextPizzaException(ErrorCode.ORDER_CANNOT_BE_CANCELLED, "Status: " + order.getOrderStatus());
        }

        if (order.getOrderStatus() == OrderStatus.CANCELLED) {
            throw new NextPizzaException(ErrorCode.ORDER_ALREADY_CANCELLED);
        }

        // Status o'zgartirish
        order.setOrderStatus(OrderStatus.CANCELLED);

        // Pulni qaytarish
        User user = order.getUser();
        user.setBalance(user.getBalance().add(order.getTotalPrice()));
        return toOrderResponse(orderRepository.save(order));
    }



    /**
     * Barcha buyurtmalar (Admin)
     */
    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAllByOrderByOrderDateDesc();
        return orders.stream().map(this::toOrderResponse).toList();
    }

    /**
     * Status bo'yicha buyurtmalar (Admin)
     */
    public List<OrderResponse> getOrdersByStatus(OrderStatus status) {
        List<Order> orders = orderRepository.findByOrderStatusOrderByOrderDateDesc(status);
        return orders.stream().map(this::toOrderResponse).toList();
    }

    /**
     * Buyurtma statusini o'zgartirish (Admin)
     */
    @Transactional
    public OrderResponse updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId).
                orElseThrow(() -> new NextPizzaException(ErrorCode.ORDER_NOT_FOUND, "ID: " + orderId));

        // Bekor qilingan buyurtma statusini o'zgartirib bo'lmaydi
        if (order.getOrderStatus() == OrderStatus.CANCELLED) {
            throw new NextPizzaException(ErrorCode.INVALID_STATUS_TRANSITION, "Bekor qilingan buyurtma");
        }

        // Tugallangan buyurtmani qayta ochib bo'lmaydi
        if (order.getOrderStatus() == OrderStatus.COMPLETED && newStatus != OrderStatus.COMPLETED) {
            throw new NextPizzaException(ErrorCode.INVALID_STATUS_TRANSITION, "Tugallangan buyurtma");
        }

        order.setOrderStatus(newStatus);
        return toOrderResponse(orderRepository.save(order));
    }

    public long allOrdersCount() {
        return orderRepository.countByIsActiveIsTrue();
    }

    public long getTodayOrdersCount() {

        LocalDate today = LocalDate.now();

        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime startOfNextDay = today.plusDays(1).atStartOfDay();

        return orderRepository
                .countByOrderDateBetweenAndIsActiveTrue(
                        startOfDay,
                        startOfNextDay
                );
    }



    private String getProductName(CartItem cartItem) {
        if (cartItem.getPizza() != null) {
            return cartItem.getPizza().getName();
        } else if (cartItem.getDrink() != null) {
            return cartItem.getDrink().getDrinkName();
        }
        return "Unknown";
    }

    private OrderResponse toOrderResponse(Order order) {
        List<OrderItemResponse> items = order.getOrderItems().
                stream().map(this::toOrderItemResponse).toList();
        String fullName =  userService.findUserFullName(order.getUser().getId());

        return OrderResponse.builder()
                .id(order.getId())
                .userId(order.getUser().getId())
                .userFullName(fullName)
                .orderDate(order.getOrderDate())
                .orderStatus(order.getOrderStatus())
                .totalPrice(order.getTotalPrice())
                .totalItems(order.getTotalItems())
                .deliveryAddress(order.getDeliveryAddress())
                .orderItems(items).build();
    }

    private OrderItemResponse toOrderItemResponse(OrderItem item) {
        return OrderItemResponse.builder()
                .id(item.getId())
                .productName(item.getProductName())
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .totalPrice(item.getTotalPrice())
                .build();
    }


}
