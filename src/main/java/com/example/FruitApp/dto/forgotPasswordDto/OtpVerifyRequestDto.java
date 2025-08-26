package com.example.FruitApp.dto.forgotPasswordDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OtpVerifyRequestDto {

    @NotBlank(message = "OTP boş ola bilməz")
    @Size(min = 6, max = 6, message = "OTP 6 simvollu olmalıdır")
    @Pattern(regexp = "\\d{6}", message = "OTP yalnız rəqəmlərdən ibarət olmalıdır")
    private String otp;

}
