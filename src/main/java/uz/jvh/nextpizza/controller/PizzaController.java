package uz.jvh.nextpizza.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.jvh.nextpizza.dto.request.PizzaRequest;
import uz.jvh.nextpizza.dto.response.PizzaResponse;
import uz.jvh.nextpizza.enomerator.PizzaType;
import uz.jvh.nextpizza.enomerator.RequestType;
import uz.jvh.nextpizza.service.FileStorageService;
import uz.jvh.nextpizza.service.PizzaService;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RequestMapping("/api/pizza")
@RestController
@RequiredArgsConstructor
public class PizzaController {

    private final PizzaService pizzaService;
    private final FileStorageService  fileStorageService;


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
    public PizzaResponse update(@PathVariable("id") Long id, @RequestBody PizzaRequest pizzaRequest) {
        return pizzaService.updateFood(id, pizzaRequest);
    }

//    @PostMapping("/create-pizza")
//    public ResponseEntity<PizzaResponse> createUser(@RequestBody PizzaRequest pizzaRequest) {
//        return ResponseEntity.ok(pizzaService.createFood(pizzaRequest));
//    }


    @DeleteMapping("/delete/{foodId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long foodId) {
        pizzaService.deleteFoodById(foodId);
        return ResponseEntity.ok("Pizza muvaffaqiyatli o'chirildi.");
    }

    @PostMapping(value = "/create-pizza", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PizzaResponse> createPizza(
            @ModelAttribute PizzaRequest pizzaRequest,
            @RequestParam("image") MultipartFile image)  throws IOException {

        String fileName = fileStorageService.saveFile(image , RequestType.PIZZA);
        pizzaRequest.setImageUrl(fileName);

        return ResponseEntity.ok(pizzaService.createFood(pizzaRequest));
    }


}
