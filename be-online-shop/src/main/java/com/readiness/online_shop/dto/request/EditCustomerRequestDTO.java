package com.readiness.online_shop.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditCustomerRequestDTO {
    @NotBlank(message = "Kolom customerName tidak boleh kosong")
    private String customerName;

    @NotBlank(message = "Kolom customerAddress tidak boleh kosong")
    private String customerAddress;

    @NotBlank(message = "Kolom customerPhone tidak boleh kosong")
    @Pattern(regexp = "^08[\\d]{9,11}", message = "Kolom customerPhone tidak sesuai format")
    private String customerPhone;

    private MultipartFile pic;

    @NotNull(message = "Kolom isActive tidak boleh kosong")
    private Boolean isActive;
}
