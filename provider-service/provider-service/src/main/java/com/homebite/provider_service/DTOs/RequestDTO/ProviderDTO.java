package com.homebite.provider_service.DTOs.RequestDTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProviderDTO {

    private String rest_name;
    private String username;
    private String fullName;
    private String email;
    private String password;
    private String rest_address;
}
