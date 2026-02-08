package uz.jvh.nextpizza.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.jvh.nextpizza.NextPizzaApplication;
import uz.jvh.nextpizza.dto.request.DrinkRequest;
import uz.jvh.nextpizza.dto.response.DrinkResponse;
import uz.jvh.nextpizza.enomerator.ErrorCode;
import uz.jvh.nextpizza.entity.Drink;
import uz.jvh.nextpizza.exception.NextPizzaException;
import uz.jvh.nextpizza.repository.DrinkRepository;

@Service
@RequiredArgsConstructor
public class DrinkService {

    private final DrinkRepository drinkRepository;

    @Transactional
    public DrinkResponse createDrink(DrinkRequest drinkRequest) {
        if(drinkRepository.existsByDrinkNameAndDrinkTypeAndVolume(drinkRequest.getDrinkName(),
                drinkRequest.getDrinkType(),drinkRequest.getVolume())){
            throw new NextPizzaException(ErrorCode.DRINK_ALREADY_EXISTS, drinkRequest.getDrinkName());
        }
        Drink drink = requestToDrink(drinkRequest);
       return drinkToResponse(drinkRepository.save(drink));
    }


    private Drink requestToDrink(DrinkRequest request) {
        return Drink.builder()
                .drinkName(request.getDrinkName())
                .price(request.getPrice())
                .volume(request.getVolume())
                .imageUrl(request.getImageUrl())
                .drinkType(request.getDrinkType()
                ).build();
    }

    private DrinkResponse drinkToResponse(Drink drink) {
        return DrinkResponse.builder()
                .drinkName(drink.getDrinkName())
                .price(drink.getPrice())
                .volume(drink.getVolume())
                .imageUrl(drink.getImageUrl())
                .drinkType(drink.getDrinkType())
                .build();
    }
}
