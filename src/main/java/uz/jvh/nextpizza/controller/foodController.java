package uz.jvh.nextpizza.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.jvh.nextpizza.service.FoodService;

@RequestMapping("/api/food")
@RestController
@RequiredArgsConstructor
public class foodController {

    private final FoodService foodService;


}
