package uz.jvh.nextpizza.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.jvh.nextpizza.dto.request.DrinkRequest;
import uz.jvh.nextpizza.dto.response.DrinkResponse;
import uz.jvh.nextpizza.enomerator.DrinkType;
import uz.jvh.nextpizza.enomerator.ErrorCode;
import uz.jvh.nextpizza.entity.Drink;
import uz.jvh.nextpizza.exception.NextPizzaException;
import uz.jvh.nextpizza.repository.DrinkRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DrinkService {

    private final DrinkRepository drinkRepository;

    @Transactional
    public DrinkResponse createDrink(DrinkRequest drinkRequest) {
        if(drinkRepository.existsByDrinkNameAndDrinkTypeAndVolumeAndIsActiveTrue(drinkRequest.getDrinkName(),
                drinkRequest.getDrinkType(),drinkRequest.getVolume())){
            throw new NextPizzaException(ErrorCode.DRINK_ALREADY_EXISTS, drinkRequest.getDrinkName());
        }
        Drink drink = requestToDrink(drinkRequest);
       return drinkToResponse(drinkRepository.save(drink));
    }

    @Transactional
    public void deleteDrink(Long id) {
        Drink drink = drinkRepository.findById(id)
                .orElseThrow(() -> new NextPizzaException(ErrorCode.DRINK_NOT_FOUND));
        drink.setActive(false);
        drinkRepository.save(drink);
    }

    @Transactional
    public DrinkResponse updateDrink(Long id, DrinkRequest drinkRequest) {
        Drink drink = drinkRepository.findById(id)
                .orElseThrow(() -> new NextPizzaException(ErrorCode.DRINK_NOT_FOUND));

        // Yangi qiymatlar (agar berilmagan bo'lsa - eski qoladi)
        String newName = drinkRequest.getDrinkName() != null ? drinkRequest.getDrinkName() : drink.getDrinkName();
        DrinkType newType = drinkRequest.getDrinkType() != null ? drinkRequest.getDrinkType() : drink.getDrinkType();
        Double newVolume = drinkRequest.getVolume() != null ? drinkRequest.getVolume() : drink.getVolume();

        boolean isChanged = !newName.equals(drink.getDrinkName()) ||
                            !newType.equals(drink.getDrinkType()) ||
                            !newVolume.equals(drink.getVolume());

        // O'zgargan bo'lsa va yangi kombinatsiya mavjud bo'lsa - xato
        if (isChanged &&
                drinkRepository.existsByDrinkNameAndDrinkTypeAndVolumeAndIsActiveTrue(newName, newType, newVolume)) {
            throw new NextPizzaException(
                    ErrorCode.DRINK_ALREADY_EXISTS,
                    String.format("%s (%s, %.1fL)", newName, newType, newVolume)  // ← %.1fL
            );
        }

        Optional.ofNullable(drinkRequest.getDrinkName()).ifPresent(drink::setDrinkName);
        Optional.ofNullable(drinkRequest.getDrinkType()).ifPresent(drink::setDrinkType);
        Optional.ofNullable(drinkRequest.getVolume()).ifPresent(drink::setVolume);
        Optional.ofNullable(drinkRequest.getPrice()).ifPresent(drink::setPrice);
        Optional.ofNullable(drinkRequest.getImageUrl()).ifPresent(drink::setImageUrl);

        drinkRepository.save(drink);
        return drinkToResponse(drink);
    }

    public DrinkResponse getDrink(Long id) {
        Drink drink = drinkRepository.findById(id).
                orElseThrow(() -> new NextPizzaException(ErrorCode.DRINK_NOT_FOUND));
        return drinkToResponse(drink);
    }

    public List<DrinkResponse> getAllDrinks() {
      return drinkRepository.findAllByIsActiveTrue()
              .stream().map(this::drinkToResponse).toList();
    }

    public  List<DrinkResponse> findByDrinkType(DrinkType drinkType) {
      return  drinkRepository.findByDrinkTypeAndIsActiveTrue(drinkType)
              .stream().map(this::drinkToResponse).toList();
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
                .id(drink.getId())
                .drinkName(drink.getDrinkName())
                .price(drink.getPrice())
                .volume(drink.getVolume())
                .imageUrl(drink.getImageUrl())
                .drinkType(drink.getDrinkType())
                .active(drink.isActive())
                .build();
    }
}
