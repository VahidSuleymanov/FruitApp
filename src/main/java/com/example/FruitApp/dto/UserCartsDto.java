package com.example.FruitApp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.YearMonth;
import java.util.UUID;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCartsDto {

    @NotNull(message = "Ödəniş sistemi seçilməlidir")
    private String nameOnCart;

    @NotBlank(message = "Kart nömrəsi boş ola bilməz")
    @Size(min = 16, max = 16, message = "Kart nömrəsi 16 simvollu olmalıdır")
    @Pattern(regexp = "\\d{16}", message = "Kart nömrəsi yalnız rəqəmlərdən ibarət olmalıdır")
    private String cardNumber;

    @Future(message = "Bitmə tarixi indiki tarixdən sonra olmalıdır")
    @JsonFormat(pattern = "MM/yy")
    @NotNull(message = "Bitme tarixi yazilmalidir")
    private YearMonth expiryDate;

    @NotBlank(message = "CVV boş ola bilməz")
    @Pattern(regexp = "\\d{3}", message = "CVV 3 rəqəm olmalıdır")
    private String cvv;

    @Builder.Default
    private boolean saveThisCart = false;

    private double totalPrice;

    private UUID statusId;
}
