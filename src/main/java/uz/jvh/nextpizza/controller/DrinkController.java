package uz.jvh.nextpizza.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.jvh.nextpizza.dto.request.DrinkRequest;
import uz.jvh.nextpizza.dto.response.DrinkResponse;
import uz.jvh.nextpizza.enomerator.DrinkType;
import uz.jvh.nextpizza.enomerator.RequestType;
import uz.jvh.nextpizza.service.DrinkService;
import uz.jvh.nextpizza.service.FileStorageService;

import java.io.IOException;
import java.util.List;

@RequestMapping("/api/drink")
@RestController
@RequiredArgsConstructor
public class DrinkController {

   private final DrinkService drinkService;
   private final FileStorageService fileStorageService;

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

    /// admin uchun end points

    @PostMapping(value = "/create-drink", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('OWNER')")
    public ResponseEntity<DrinkResponse> createDrink(
            @ModelAttribute DrinkRequest drinkRequest,
            @RequestParam DrinkType drinkType,
            @RequestParam("image") MultipartFile image)  throws IOException {

        drinkRequest.setDrinkType(drinkType);
        String fileName = fileStorageService.saveFile(image , RequestType.DRINK);
        drinkRequest.setImageUrl(fileName);

        return ResponseEntity.ok(drinkService.createDrink(drinkRequest));
    }

    @PostMapping(value = "/update-drink/{id}" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('OWNER')")
    public ResponseEntity<DrinkResponse> updateDrink(@PathVariable("id") Long id ,
                                                     @ModelAttribute DrinkRequest drinkRequest,
                                                     @RequestParam DrinkType drinkType,
                                                     @RequestParam("image") MultipartFile image)  throws IOException{
       drinkRequest.setDrinkType(drinkType);
        return ResponseEntity.ok(drinkService.updateDrink(id, drinkRequest , image));
    }

    @DeleteMapping("/delete-drink/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('OWNER')")
    public ResponseEntity<String> deleteDrink(@PathVariable("id") Long id) {
       drinkService.deleteDrink(id);
       return ResponseEntity.ok("Drink muvaffaqiyatli o'chirildi");
    }

    @GetMapping("/find-all-for-admin")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('OWNER')")
    public ResponseEntity<List<DrinkResponse>> findAllForAdmin() {
        return ResponseEntity.ok(drinkService.getAllDrinksForAdmin());
    }
}
