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
    @NotBlank(message = "Kolom itemName tidak boleh kosong")
    private String itemName;

    @NotNull(message = "Kolom stock tidak boleh kosong")
    @Min(value = 0, message = "Stock tidak boleh bernilai kurang dari 0")
    private Integer stock;

    @NotNull(message = "Kolom price tidak boleh kosong")
    @Min(value = 0, message = "Price tidak boleh bernilai kurang dari 0")
    private Long price;

    @NotNull(message = "Kolom isAvailable tidak boleh kosong")
    private Boolean isAvailable;
}
