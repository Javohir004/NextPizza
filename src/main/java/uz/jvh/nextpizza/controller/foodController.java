package uz.jvh.nextpizza.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.jvh.nextpizza.domain.dto.request.FoodRequest;
import uz.jvh.nextpizza.domain.dto.request.UserRequest;
import uz.jvh.nextpizza.domain.dto.response.FoodResponse;
import uz.jvh.nextpizza.domain.dto.response.UserResponse;
import uz.jvh.nextpizza.domain.enomerator.FoodType;
import uz.jvh.nextpizza.domain.entity.User;
import uz.jvh.nextpizza.service.FoodService;

import java.util.List;
import java.util.UUID;

@RequestMapping("/api/food")
@RestController
@RequiredArgsConstructor
public class foodController {

    private final FoodService foodService;

    @GetMapping("/search")
    public ResponseEntity<List<FoodResponse>> searchFoods(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) FoodType foodType
    ) {
        return ResponseEntity.ok(foodService.searchFoods(name, minPrice, maxPrice, foodType));
    }


    @PutMapping("/update/{id}")
    public FoodResponse updateUser(@PathVariable("id") UUID id, @RequestBody FoodRequest foodRequest) {
        return foodService.updateFood(id, foodRequest);
    }


    @PostMapping("/create-food")
    public ResponseEntity<FoodResponse> createUser(@RequestBody FoodRequest foodRequest) {
        return ResponseEntity.ok(foodService.createFood(foodRequest));
    }


    @DeleteMapping("/delete/{foodId}")
    public ResponseEntity<String> deleteUser(@PathVariable UUID foodId) {
        foodService.deleteFoodById(foodId);
        return ResponseEntity.ok("Foydalanuvchi muvaffaqiyatli o'chirildi.");
    }


}
