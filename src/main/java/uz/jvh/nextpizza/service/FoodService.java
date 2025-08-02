package uz.jvh.nextpizza.service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.jvh.nextpizza.domain.dto.request.FoodRequest;
import uz.jvh.nextpizza.domain.dto.response.FoodResponse;
import uz.jvh.nextpizza.domain.entity.Food;
import uz.jvh.nextpizza.repository.FoodRepository;

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
