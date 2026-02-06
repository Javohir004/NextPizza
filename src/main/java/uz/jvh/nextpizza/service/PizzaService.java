package uz.jvh.nextpizza.service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.jvh.nextpizza.dto.request.PizzaRequest;
import uz.jvh.nextpizza.dto.response.PizzaResponse;
import uz.jvh.nextpizza.enomerator.PizzaType;
import uz.jvh.nextpizza.entity.Pizza;
import uz.jvh.nextpizza.repository.FoodRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PizzaService {

    private final FoodRepository foodRepository;

    @Transactional
    public PizzaResponse createFood(PizzaRequest pizzaRequest) {
        return toPizzaResponse(
                foodRepository.save(
                        mapRequestToEntity(pizzaRequest)));
    }

    public Pizza findFoodById(UUID id) {
        return foodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pizza topilmadi: " + id));
    }

    public List<PizzaResponse> searchFoods(String name, BigDecimal minPrice, BigDecimal maxPrice, PizzaType pizzaType) {
        List<Pizza> pizzas = foodRepository.searchFoods(name, minPrice, maxPrice, pizzaType);
        return pizzas.stream()
                .map(this::toPizzaResponse)
                .toList();
    }

    @Transactional
    public void deleteFoodById(UUID id) {
        Pizza pizza = findFoodById(id);
        pizza.setActive(false);
        foodRepository.save(pizza);
        // flush() kerak emas - @Transactional avtomatik commit qiladi
    }

    @Transactional
    public PizzaResponse updateFood(UUID foodId, PizzaRequest pizzaRequest) {
        Pizza pizza = findFoodById(foodId);

        if (pizzaRequest == null) {
            return toPizzaResponse(pizza);
        }

        Optional.ofNullable(pizzaRequest.getName()).ifPresent(pizza::setName);
        Optional.ofNullable(pizzaRequest.getDescription()).ifPresent(pizza::setDescription);
        Optional.ofNullable(pizzaRequest.getPrice()).ifPresent(pizza::setPrice);
        Optional.ofNullable(pizzaRequest.getPizzaType()).ifPresent(pizza::setPizzaType);
        Optional.ofNullable(pizzaRequest.getImageUrl()).ifPresent(pizza::setImageUrl);

        return toPizzaResponse(foodRepository.save(pizza));
    }


    // Barcha faol pitsalarni type bo'yicha guruhlash
    public Map<PizzaType, List<PizzaResponse>> getAllPizzas() {
        List<Pizza> pizzas = foodRepository.findAllByIsActiveTrueOrderByPizzaTypeAscNameAsc();

        return pizzas.stream()
                .map(this::toPizzaResponse)
                .collect(Collectors.groupingBy(PizzaResponse::getPizzaType));
    }

    // Oddiy list (type bo'yicha saralangan)
    public List<PizzaResponse> getAllPizzasList() {
        List<Pizza> pizzas = foodRepository.findAllByIsActiveTrueOrderByPizzaTypeAscPriceAsc();

        return pizzas.stream()
                .map(this::toPizzaResponse)
                .collect(Collectors.toList());
    }

    // Bitta type bo'yicha
    public List<PizzaResponse> getPizzasByType(PizzaType type) {
        List<Pizza> pizzas = foodRepository.findAllByIsActiveTrueAndPizzaTypeOrderByPriceAsc(type);

        return pizzas.stream()
                .map(this::toPizzaResponse)
                .collect(Collectors.toList());
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
                .foodId(pizza.getId())
                .name(pizza.getName())
                .pizzaType(pizza.getPizzaType())
                .price(pizza.getPrice())
                .description(pizza.getDescription())
                .imageUrl(pizza.getImageUrl())
                .createDate(pizza.getCreated())
                .build();
    }
}
