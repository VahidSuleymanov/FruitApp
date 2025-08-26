package com.example.FruitApp.dto;

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
public class KemiyyetDto {

    @NotBlank(message = "Ad bos ola bilmez")
    private String name;
    @NotBlank(message = "Abreviatura bos ola bilmez")
    private String abbv;
    @NotNull(message = "Status seçilməlidir")
    private UUID statusId;

}
