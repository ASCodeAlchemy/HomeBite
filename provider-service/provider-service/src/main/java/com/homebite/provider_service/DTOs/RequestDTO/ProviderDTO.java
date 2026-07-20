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

    private Long providerId;

    private String restName;
    private String fullName;
    private String email;
    private String password;
    private String restAddress;
}
