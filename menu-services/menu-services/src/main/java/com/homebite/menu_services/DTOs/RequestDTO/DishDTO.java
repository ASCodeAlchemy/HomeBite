package com.homebite.menu_services.DTOs.RequestDTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class DishDTO {

    private String dishName;
    private Double price;
    private Boolean veg;
    private String imageFileName;
}
