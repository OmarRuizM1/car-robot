package com.example.carrobot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@ToString
public class Car {
    private Long id;
    private String brand;
    private String model;
    private String color;
    private Double price;
}
