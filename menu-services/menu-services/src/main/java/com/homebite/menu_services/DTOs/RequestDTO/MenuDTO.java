package com.homebite.menu_services.DTOs.RequestDTO;


import com.homebite.menu_services.Entity.Dishes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MenuDTO {

    private Long providerId;

    private List<Dishes> dishes = new ArrayList<>();
}
