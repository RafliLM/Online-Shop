package com.readiness.online_shop.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddOrderRequestDTO {
    @NotNull(message = "Field customerId can't be empty")
    private Long customerId;

    @NotNull(message = "Field itemId can't be empty")
    private Long itemId;

    @NotNull(message = "Field quantity can't be empty")
    @Min(value = 1, message = "Quantity can't be less than 1")
    private Integer quantity;
}
