package uz.jvh.nextpizza.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.jvh.nextpizza.enomerator.DrinkType;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DrinkResponse {
    private String drinkName;
    private DrinkType drinkType;
    private Double volume;
    private BigDecimal price;
    private String imageUrl;
}
