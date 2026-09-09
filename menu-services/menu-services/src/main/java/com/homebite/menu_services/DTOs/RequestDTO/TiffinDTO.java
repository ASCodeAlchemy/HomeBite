package com.homebite.menu_services.DTOs.RequestDTO;


import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class TiffinDTO {

    @JsonAlias("dishId")
    private Long tiffinId;
    private String dishName;
    private Double price;
    private Boolean veg;
    private String imageFileName;
    private String imageUrl;
}
