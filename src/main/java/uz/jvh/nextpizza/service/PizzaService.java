package uz.jvh.nextpizza.service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.jvh.nextpizza.dto.request.PizzaRequest;
import uz.jvh.nextpizza.dto.response.PizzaResponse;
import uz.jvh.nextpizza.enomerator.ErrorCode;
import uz.jvh.nextpizza.enomerator.PizzaType;
import uz.jvh.nextpizza.entity.Pizza;
import uz.jvh.nextpizza.exception.NextPizzaException;
import uz.jvh.nextpizza.repository.PizzaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PizzaService {

    private final PizzaRepository pizzaRepository;

    @Transactional
    public PizzaResponse createFood(PizzaRequest pizzaRequest) {
        if (pizzaRepository.existsByNameIgnoreCase(pizzaRequest.getName())) {
            throw new NextPizzaException(ErrorCode.PIZZA_ALREADY_EXISTS ,"Pizza Name: " + pizzaRequest.getName());
        }

        return toPizzaResponse(
                pizzaRepository.save(
                        mapRequestToEntity(pizzaRequest)));
    }

    public List<PizzaResponse> searchFoods(String name, BigDecimal minPrice, BigDecimal maxPrice, PizzaType pizzaType) {
        List<Pizza> pizzas = pizzaRepository.searchFoods(name, minPrice, maxPrice, pizzaType);
        return pizzas.stream()
                .map(this::toPizzaResponse)
                .toList();
    }

    @Transactional
    public void deleteFoodById(Long id) {
        Pizza pizza = pizzaRepository.findById(id)
                .orElseThrow(() -> new NextPizzaException(ErrorCode.PIZZA_NOT_FOUND ,"ID: " + id));
        pizza.setActive(false);
        pizzaRepository.save(pizza);
        // flush() kerak emas - @Transactional avtomatik commit qiladi
    }

    @Transactional
    public PizzaResponse updateFood(Long id, PizzaRequest pizzaRequest) {
        Pizza pizza = pizzaRepository.findById(id)
                .orElseThrow(() -> new NextPizzaException(ErrorCode.PIZZA_NOT_FOUND ,"ID: " + id));

        if (pizzaRequest == null) {
            return toPizzaResponse(pizza);
        }

        Optional.ofNullable(pizzaRequest.getName()).ifPresent(pizza::setName);
        Optional.ofNullable(pizzaRequest.getDescription()).ifPresent(pizza::setDescription);
        Optional.ofNullable(pizzaRequest.getPrice()).ifPresent(pizza::setPrice);
        Optional.ofNullable(pizzaRequest.getPizzaType()).ifPresent(pizza::setPizzaType);
        Optional.ofNullable(pizzaRequest.getImageUrl()).ifPresent(pizza::setImageUrl);

        return toPizzaResponse(pizzaRepository.save(pizza));
    }


    // Barcha faol pitsalarni type bo'yicha guruhlash
    public Map<PizzaType, List<PizzaResponse>> getAllPizzas() {
        List<Pizza> pizzas = pizzaRepository.findAllByIsActiveTrueOrderByPizzaTypeAscNameAsc();

        return pizzas.stream()
                .map(this::toPizzaResponse)
                .collect(Collectors.groupingBy(PizzaResponse::getPizzaType));
    }

    // Oddiy list (type bo'yicha saralangan)
    public List<PizzaResponse> getAllPizzasList() {
        List<Pizza> pizzas = pizzaRepository.findAllByIsActiveTrueOrderByPizzaTypeAscPriceAsc();

        return pizzas.stream()
                .map(this::toPizzaResponse)
                .collect(Collectors.toList());
    }

    // Bitta type bo'yicha
    public List<PizzaResponse> getPizzasByType(PizzaType type) {
        List<Pizza> pizzas = pizzaRepository.findAllByIsActiveTrueAndPizzaTypeOrderByPriceAsc(type);

        return pizzas.stream()
                .map(this::toPizzaResponse)
                .collect(Collectors.toList());
    }

    public Pizza findById(Long id) {
      return  pizzaRepository.findById(id).
              orElseThrow(() -> new NextPizzaException(ErrorCode.PIZZA_NOT_FOUND ,"ID: " + id));
    }


    public Pizza mapRequestToEntity(PizzaRequest pizzaRequest) {
        return Pizza.builder()
                .name(pizzaRequest.getName())
                .pizzaType(pizzaRequest.getPizzaType())
                .price(pizzaRequest.getPrice())
                .description(pizzaRequest.getDescription())
                .imageUrl(pizzaRequest.getImageUrl())
                .build();
    }


    public PizzaResponse toPizzaResponse(Pizza pizza) {
        return PizzaResponse.builder()
                .pizzaId(pizza.getId())
                .name(pizza.getName())
                .pizzaType(pizza.getPizzaType())
                .price(pizza.getPrice())
                .description(pizza.getDescription())
                .imageUrl(pizza.getImageUrl())
                .createDate(pizza.getCreated())
                .build();
    }

}
