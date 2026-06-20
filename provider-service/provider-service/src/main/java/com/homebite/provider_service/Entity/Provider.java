package com.homebite.provider_service.Entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "provider_details")
public class Provider {

    @Id
    private int provider_id;
    private String rest_name;
    private String username;
    private String fullName;
    private String email;
    private String password;
    private String rest_address;


}
