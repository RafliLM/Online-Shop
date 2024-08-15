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
public class AddCustomerRequestDTO {
    @NotBlank(message = "Column customerName can't be empty")
    private String customerName;

    @NotBlank(message = "Column customerAddress can't be empty")
    private String customerAddress;

    @NotBlank(message = "Column customerPhone can't be empty")
    @Pattern(regexp = "^08[\\d]{9,11}", message = "Column customerPhone doesn't match the format")
    private String customerPhone;

    @NotNull(message = "Column pic can't be empty")
    private MultipartFile pic;
}
