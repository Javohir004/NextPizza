package uz.jvh.nextpizza.dto.request;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class DrinkRequest {
    @NotBlank(message = "Ichimlik nomi kiritilishi shart")  // ← @NotNull emas!
    private String drinkName;

    @NotNull(message = "Ichimlik turi tanlanishi shart")
    private DrinkType drinkType;

    @NotNull(message = "Hajm kiritilishi shart")
    @DecimalMin(value = "0.0", inclusive = false, message = "Hajm 0 dan katta bo'lishi kerak")
    private Double volume;  // ← double emas, Double (yoki Integer)

    @NotNull(message = "Narx kiritilishi shart")
    @DecimalMin(value = "0.0", inclusive = false, message = "Narx 0 dan katta bo'lishi kerak")
    private BigDecimal price;

    @NotBlank(message = "Ichimlik nomi kiritilishi shart")
    private String imageUrl;
}
