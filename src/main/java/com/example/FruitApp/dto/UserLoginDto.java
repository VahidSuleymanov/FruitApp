package com.example.FruitApp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDto {

    private String password;
    private String email;
    private boolean rememberPassword;
}
