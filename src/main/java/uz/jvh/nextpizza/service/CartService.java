package uz.jvh.nextpizza.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.jvh.nextpizza.repository.CartRepository;


@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

}
