package com.readiness.online_shop.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestDTO {
    @NotBlank(message = "Field itemName can't be empty")
    private String itemName;

    @NotNull(message = "Field stock can't be empty")
    @Min(value = 0, message = "Stock can't be less than 0")
    private Integer stock;

    @NotNull(message = "Field price can't be empty")
    @Min(value = 0, message = "Price can't be less than 0")
    private Long price;

    @NotNull(message = "Field isAvailable can't be empty")
    private Boolean isAvailable;
}
