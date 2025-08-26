package com.example.FruitApp.dto.forgotPasswordDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ForgotPasswordRequestDto {

    @NotBlank(message = "Email bos ola bilmez")
    @Email(message = "Zəhmət olmasa düzgün email formatı daxil edin")
    private String email;

}
