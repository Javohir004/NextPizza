package uz.jvh.nextpizza.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.jvh.nextpizza.dto.request.PizzaRequest;
import uz.jvh.nextpizza.dto.response.PizzaResponse;
import uz.jvh.nextpizza.enomerator.PizzaType;
import uz.jvh.nextpizza.service.PizzaService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequestMapping("/api/pizza")
@RestController
@RequiredArgsConstructor
public class PizzaController {

    private final PizzaService pizzaService;

    @GetMapping("/search")
    public ResponseEntity<List<PizzaResponse>> searchFoods(@RequestParam(required = false) String name,
                                                           @RequestParam(required = false) BigDecimal minPrice,
                                                           @RequestParam(required = false) BigDecimal maxPrice,
                                                           @RequestParam(required = false) PizzaType pizzaType) {
        return ResponseEntity.ok(pizzaService.searchFoods(name, minPrice, maxPrice, pizzaType));
    }

    // Barcha pitsalar (type bo'yicha guruhlangan)
    @GetMapping("/grouped")
    public ResponseEntity<Map<PizzaType, List<PizzaResponse>>> getPizzasGrouped() {
        return ResponseEntity.ok(pizzaService.getAllPizzas());
    }

    // Oddiy list
    @GetMapping
    public ResponseEntity<List<PizzaResponse>> getAllPizzas() {
        return ResponseEntity.ok(pizzaService.getAllPizzasList());
    }

    // Type bo'yicha filter
    @GetMapping("/type/{type}")
    public ResponseEntity<List<PizzaResponse>> getPizzasByType(@PathVariable PizzaType type) {
        return ResponseEntity.ok(pizzaService.getPizzasByType(type));
    }

    @PutMapping("/update/{id}")
    public PizzaResponse update(@PathVariable("id") UUID id, @RequestBody PizzaRequest pizzaRequest) {
        return pizzaService.updateFood(id, pizzaRequest);
    }

    @PostMapping("/create-food")
    public ResponseEntity<PizzaResponse> createUser(@RequestBody PizzaRequest pizzaRequest) {
        return ResponseEntity.ok(pizzaService.createFood(pizzaRequest));
    }


    @DeleteMapping("/delete/{foodId}")
    public ResponseEntity<String> deleteUser(@PathVariable UUID foodId) {
        pizzaService.deleteFoodById(foodId);
        return ResponseEntity.ok("Foydalanuvchi muvaffaqiyatli o'chirildi.");
    }


}
