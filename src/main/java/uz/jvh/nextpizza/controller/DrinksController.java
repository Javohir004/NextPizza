package uz.jvh.nextpizza.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.jvh.nextpizza.dto.request.DrinkRequest;
import uz.jvh.nextpizza.dto.response.DrinkResponse;
import uz.jvh.nextpizza.enomerator.DrinkType;
import uz.jvh.nextpizza.service.DrinkService;

import java.util.List;

@RequestMapping("/api/drink")
@RestController
@RequiredArgsConstructor
public class DrinksController {

   private final DrinkService drinkService;

   @PostMapping("/create-drink")
   public ResponseEntity<DrinkResponse> createDrink(@RequestBody DrinkRequest drinkRequest) {
     return ResponseEntity.ok(drinkService.createDrink(drinkRequest));
   }

    @PutMapping("/update-drink/{id}")
    public ResponseEntity<DrinkResponse> updateDrink(@PathVariable("id") Long id ,@RequestBody DrinkRequest drinkRequest) {
       return ResponseEntity.ok(drinkService.updateDrink(id,drinkRequest));
    }

    @DeleteMapping("/delete-drink/{id}")
    public ResponseEntity<String> deleteDrink(@PathVariable("id") Long id) {
       drinkService.deleteDrink(id);
       return ResponseEntity.ok("Drink muvaffaqiyatli o'chirildi");
    }

    @GetMapping("/findby-id/{id}")
    public ResponseEntity<DrinkResponse> findById(@PathVariable("id") Long id) {
       return ResponseEntity.ok(drinkService.getDrink(id));
    }

    @GetMapping("/get-by-type/{type}")
    public ResponseEntity<List<DrinkResponse>> getByType(@PathVariable("type") DrinkType type) {
       return ResponseEntity.ok(drinkService.findByDrinkType(type));
    }

    @GetMapping("/find-all")
    public ResponseEntity<List<DrinkResponse>> findAll() {
       return ResponseEntity.ok(drinkService.getAllDrinks());
    }
}
