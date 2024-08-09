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
public class EditOrderRequestDTO {
    @NotNull(message = "Kolom itemId tidak boleh kosong")
    private Long itemId;

    @NotNull(message = "kolom quantity tidak boleh kosong")
    @Min(value = 1, message = "Quantity tidak boleh kurang dari 1")
    private Integer quantity;
}
