package com.homebite.driver_service.Entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "drivers")
public class Driver {

    @Id
    private UUID driverId;
    private String email;
    private String username;
    private String password;
    private String address;
    private String phoneNo;
    private long licenceNo;

}
