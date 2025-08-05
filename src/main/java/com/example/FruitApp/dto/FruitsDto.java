package com.example.FruitApp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FruitsDto {

    private String name;
    private double qiymet;
    private double miqdar;
}
