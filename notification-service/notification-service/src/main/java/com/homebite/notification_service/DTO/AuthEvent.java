package com.homebite.notification_service.DTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthEvent {

    private String otp;
    private String email;
    private String type;
    private String username;
}
