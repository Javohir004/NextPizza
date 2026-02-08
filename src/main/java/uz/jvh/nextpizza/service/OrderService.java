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


}
