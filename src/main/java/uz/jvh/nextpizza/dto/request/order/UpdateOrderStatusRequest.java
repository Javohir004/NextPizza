package uz.jvh.nextpizza.dto.request.order;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import uz.jvh.nextpizza.enomerator.OrderStatus;

@Data
public class UpdateOrderStatusRequest {

    @NotNull(message = "Status kiritilishi shart")
    private OrderStatus status;
}