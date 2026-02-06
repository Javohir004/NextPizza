package uz.jvh.nextpizza.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.jvh.nextpizza.dto.request.FoodRequest;
import uz.jvh.nextpizza.dto.response.FoodResponse;
import uz.jvh.nextpizza.enomerator.FoodType;
import uz.jvh.nextpizza.service.FoodService;

import java.util.List;
import java.util.UUID;

@RequestMapping("/api/food")
@RestController
@RequiredArgsConstructor
public class foodController {

    private final FoodService foodService;

    @GetMapping("/search")
    public ResponseEntity<List<FoodResponse>> searchFoods(@RequestParam(required = false) String name,
                                                          @RequestParam(required = false) Double minPrice,
                                                          @RequestParam(required = false) Double maxPrice,
                                                          @RequestParam(required = false) FoodType foodType) {
        return ResponseEntity.ok(foodService.searchFoods(name, minPrice, maxPrice, foodType));
    }

    @GetMapping("/get-foods")
    public ResponseEntity<List<FoodResponse>> searchFoods(){
       return ResponseEntity.ok(foodService.getFood()) ;
    }


    @PutMapping("/update/{id}")
    public FoodResponse update(@PathVariable("id") UUID id,
                                   @RequestBody FoodRequest foodRequest) {
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
