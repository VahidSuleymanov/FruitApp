package com.example.FruitApp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSignUpDto {

    private String email;
    private String username;
    private String password;
    private String confirmPassword;
    private boolean rememberPassword;

}
