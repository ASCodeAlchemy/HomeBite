package com.homebite.user_service.DTOs.RequestDTO;


import jdk.jfr.StackTrace;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@StackTrace
@AllArgsConstructor
@NoArgsConstructor
public class DishDTO {

    private String dishName;
    private Double price;
    private Boolean veg;
    private String imageFileName;
    private String imageUrl;
}
