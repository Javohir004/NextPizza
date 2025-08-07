package uz.jvh.nextpizza.service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.jvh.nextpizza.domain.dto.request.FoodRequest;
import uz.jvh.nextpizza.domain.dto.response.FoodResponse;
import uz.jvh.nextpizza.domain.entity.Food;
import uz.jvh.nextpizza.domain.exception.CustomException;
import uz.jvh.nextpizza.repository.FoodRepository;

import java.util.UUID;

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

    public Food findFoodById(UUID id) {
        return foodRepository.findById(id).
                orElseThrow(() -> new CustomException("Food not found", 4002, HttpStatus.NOT_FOUND));
    }


    @Transactional
    public FoodResponse updateFood(UUID foodId , FoodRequest foodRequest) {
        Food food = findFoodById(foodId);

        if(foodRequest == null) {
            return mapEntityToResponse(food);
        }

        food.setName(foodRequest.getName() != null ? foodRequest.getName() : food.getName());
        food.setDescription(foodRequest.getDescription() != null ? foodRequest.getDescription() : food.getDescription());
        food.setPrice(foodRequest.getPrice() != null ? foodRequest.getPrice() : food.getPrice());
        food.setFoodType(foodRequest.getFoodType() != null ? foodRequest.getFoodType() : food.getFoodType());

        return mapEntityToResponse(foodRepository.save(food));

    }




    public Food mapRequestToEntity(FoodRequest foodRequest) {
        return Food.builder()
                .name(foodRequest.getName())
                .foodType(foodRequest.getFoodType())
                .price(foodRequest.getPrice())
                .description(foodRequest.getDescription())
                .build();

    }


    public FoodResponse mapEntityToResponse(Food food) {
        return FoodResponse.builder()
                .foodId(food.getId())
                .name(food.getName())
                .foodType(food.getFoodType())
                .price(food.getPrice())
                .description(food.getDescription())
                .createDate(food.getCreated())
                .build();
    }
}
