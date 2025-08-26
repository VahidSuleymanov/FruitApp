package com.example.FruitApp.dto.forgotPasswordDto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordRequestDto {

    @NotBlank(message = "Password bos ola bilmez")
    private String newPassword;
    private String confirmPassword;

}
