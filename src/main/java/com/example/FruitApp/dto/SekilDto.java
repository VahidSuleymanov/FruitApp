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
public class SekilDto {

    @NotBlank(message = "Ad bos ola bilmez")
    private String name;
    @NotNull(message = "Mehsul seçilməlidir")
    private UUID fruitsId;
    @NotNull(message = "Status seçilməlidir")
    private UUID statusId;

}
