package uz.jvh.nextpizza.service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.jvh.nextpizza.dto.request.FoodRequest;
import uz.jvh.nextpizza.dto.response.FoodResponse;
import uz.jvh.nextpizza.enomerator.FoodType;
import uz.jvh.nextpizza.entity.Pizza;
import uz.jvh.nextpizza.repository.FoodRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FoodService {

    private final FoodRepository foodRepository;

    @Transactional
    public FoodResponse createFood(FoodRequest foodRequest) {
        return mapEntityToResponse(
                  foodRepository.save(
                          mapRequestToEntity(foodRequest)));
    }

    public Pizza findFoodById(UUID id) {
        return foodRepository.findById(id).orElseThrow(null);
    }

    public List<FoodResponse> searchFoods(String name, Double minPrice, Double maxPrice, FoodType foodType) {
        List<Pizza> pizzas = foodRepository.searchFoods(name, minPrice, maxPrice, foodType);
        return pizzas.stream()
                .map(this::mapEntityToResponse)
                .collect(Collectors.toList());
    }


    public void deleteFoodById(UUID id) {
        Pizza pizzaById = findFoodById(id);
        pizzaById.setActive(false);
        foodRepository.save(pizzaById);
        foodRepository.flush();
    }


    @Transactional
    public FoodResponse updateFood(UUID foodId , FoodRequest foodRequest) {
        Pizza pizza = findFoodById(foodId);

        if(foodRequest == null) {
            return mapEntityToResponse(pizza);
        }

        pizza.setName(foodRequest.getName() != null ? foodRequest.getName() : pizza.getName());
        pizza.setDescription(foodRequest.getDescription() != null ? foodRequest.getDescription() : pizza.getDescription());
        pizza.setPrice(foodRequest.getPrice() != null ? foodRequest.getPrice() : pizza.getPrice());
        pizza.setFoodType(foodRequest.getFoodType() != null ? foodRequest.getFoodType() : pizza.getFoodType());

        return mapEntityToResponse(foodRepository.save(pizza));

    }


    public Pizza mapRequestToEntity(FoodRequest foodRequest) {
        return Pizza.builder()
                .name(foodRequest.getName())
                .foodType(foodRequest.getFoodType())
                .price(foodRequest.getPrice())
                .description(foodRequest.getDescription())
                .build();

    }


    public FoodResponse mapEntityToResponse(Pizza pizza) {
        return FoodResponse.builder()
                .foodId(pizza.getId())
                .name(pizza.getName())
                .foodType(pizza.getFoodType())
                .price(pizza.getPrice())
                .description(pizza.getDescription())
                .createDate(pizza.getCreated())
                .build();
    }
}
