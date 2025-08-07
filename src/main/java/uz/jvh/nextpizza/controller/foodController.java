package uz.jvh.nextpizza.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uz.jvh.nextpizza.domain.dto.response.FoodResponse;
import uz.jvh.nextpizza.domain.enomerator.FoodType;
import uz.jvh.nextpizza.service.FoodService;

import java.util.List;

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



}
