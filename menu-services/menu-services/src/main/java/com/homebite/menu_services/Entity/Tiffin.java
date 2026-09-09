package com.homebite.menu_services.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name= "dishes")
public class Tiffin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tiffinId;

    private String dishName;
    private Double price;
    private Boolean veg;
    private String imageFileName;

    @Transient
    private String imageUrl;


}
