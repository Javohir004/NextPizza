package uz.jvh.nextpizza.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.jvh.nextpizza.dto.request.OrderRequest;
import uz.jvh.nextpizza.dto.response.OrderResponse;
import uz.jvh.nextpizza.entity.Order;
import uz.jvh.nextpizza.repository.OrderRepository;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;




    public Order mapRequestToOrder(OrderRequest orderRequest) {
        return Order.builder()
                .orderDate(orderRequest.getOrderDate())
                .deliveryAddress(orderRequest.getDeliveryAddress())
                .price(orderRequest.getPrice())
                .pizzas(orderRequest.getPizzas())
                .user(orderRequest.getUser())
                .build();
    }

    public OrderResponse mapOrderToResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .orderDate(order.getOrderDate())
                .deliveryAddress(order.getDeliveryAddress())
                .price(order.getPrice())
                .pizzas(order.getPizzas())
                .user(order.getUser())
                .build();
    }

}
