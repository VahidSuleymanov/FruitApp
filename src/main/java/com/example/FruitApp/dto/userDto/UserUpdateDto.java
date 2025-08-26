package com.example.FruitApp.dto.userDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDto {

    @NotBlank(message = "Email bos ola bilmez")
    private String email;
    @NotBlank(message = "Password bos ola bilmez")
    private String password;
    @NotNull(message = "Status seçilməlidir")
    private UUID statusId;

}
