package com.example.FruitApp.dto.forgotPasswordDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ForgotPasswordTokenResponseDto {

    private String token;

}
