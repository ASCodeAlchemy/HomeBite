package com.homebite.driver_service.DTOs.RequestDTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class DriverDTO {

    private String username;
    private String email;
    private String password;
    private String address;
    private String phoneNo;
    private long licenceNo;

}
