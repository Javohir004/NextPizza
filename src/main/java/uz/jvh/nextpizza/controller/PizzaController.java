package uz.jvh.nextpizza.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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


    @GetMapping("/grouped")
    public ResponseEntity<Map<PizzaType, List<PizzaResponse>>> getPizzasGrouped() {
        return ResponseEntity.ok(pizzaService.getAllPizzas());
    }


    @GetMapping
    public ResponseEntity<List<PizzaResponse>> getAllPizzas() {
        return ResponseEntity.ok(pizzaService.getAllPizzasList());
    }

    @GetMapping("/pizza/{id}")
    public ResponseEntity<PizzaResponse> getPizzaById(@PathVariable Long id) {
        return ResponseEntity.ok(pizzaService.getById(id));
    }


    @GetMapping("/type/{type}")
    public ResponseEntity<List<PizzaResponse>> getPizzasByType(@PathVariable PizzaType type) {
        return ResponseEntity.ok(pizzaService.getPizzasByType(type));
    }

    /// admin uchun end pointlar

    @GetMapping("/all-pizza-for-admin")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('OWNER')")
    public ResponseEntity<List<PizzaResponse>> getAllPizzasForAdmin() {
        return ResponseEntity.ok(pizzaService.getAllPizzasListForAdmin());
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('OWNER')")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        pizzaService.deleteFoodById(id);
        return ResponseEntity.ok("Pizza muvaffaqiyatli o'chirildi.");
    }

    @PostMapping(value = "/update-pizza/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('OWNER')")
    public PizzaResponse update(@PathVariable("id") Long id,
                                @ModelAttribute PizzaRequest pizzaRequest ,
                                @RequestParam PizzaType pizzaType,
                                @RequestParam("image") MultipartFile image) throws IOException {

        pizzaRequest.setPizzaType(pizzaType);
        return pizzaService.updateFood(id, pizzaRequest , image);
    }

    @PostMapping(value = "/create-pizza", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('OWNER')")
    public ResponseEntity<PizzaResponse> createPizza(
            @ModelAttribute PizzaRequest pizzaRequest,
            @RequestParam PizzaType pizzaType,
            @RequestParam("image") MultipartFile image)  throws IOException {

        pizzaRequest.setPizzaType(pizzaType);
        String fileName = fileStorageService.saveFile(image , RequestType.PIZZA);
        pizzaRequest.setImageUrl(fileName);

        return ResponseEntity.ok(pizzaService.createFood(pizzaRequest));
    }


}
