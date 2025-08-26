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
public class FruitsDto {

    @NotBlank(message = "Ad bos ola bilmez")
    private String name;
    private double qiymet;
    private int miqdar;
    @NotNull(message = "Kataqoriya seçilməlidir")
    private UUID kataqoriyaId;
    @NotNull(message = "Kemiyyet seçilməlidir")
    private UUID kemiyyetId;
    @NotNull(message = "Valyuta seçilməlidir")
    private UUID valyutaId;
    @NotNull(message = "Status seçilməlidir")
    private UUID statusId;

}
