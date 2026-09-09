package com.homebite.driver_service.Services;


import com.homebite.driver_service.DTOs.RequestDTO.DriverDTO;
import com.homebite.driver_service.DTOs.ResponseDTO.ResponseDTO;
import com.homebite.driver_service.Entity.Driver;
import com.homebite.driver_service.Repositories.DriverRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DriverService {

    private final DriverRepo driverRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DriverService(DriverRepo driverRepo, PasswordEncoder passwordEncoder) {
        this.driverRepo = driverRepo;
        this.passwordEncoder = passwordEncoder;
    }


    public Driver register(DriverDTO driverDTO) {

        if(driverDTO.getPassword()==null || driverDTO.getPassword().isEmpty()){
            throw new IllegalArgumentException("Password cannot be null");
        }
        Driver driver = new Driver();

        driver.setEmail(driverDTO.getEmail());
        driver.setUsername(driverDTO.getUsername());
        driver.setPhoneNo(driverDTO.getPhoneNo());
        driver.setPassword(passwordEncoder.encode(driverDTO.getPassword()));
        driver.setAddress(driverDTO.getAddress());
        driver.setLicenceNo(driverDTO.getLicenceNo());
        return driver;
    }

    public ResponseDTO signup(DriverDTO driverDTO){

        Optional<Driver>  driverEmail= driverRepo.findDriverByEmail(driverDTO.getEmail());

        if(driverEmail.isPresent()){
            throw new IllegalArgumentException("Driver with this email already exists");
        }

        Driver driver = register(driverDTO);
        driverRepo.save(driver);
        ResponseDTO dto = new ResponseDTO();

        dto.setMessage("Driver Registered Successfully");

        return dto; 

    }


    public boolean emailExists(String email){
        return driverRepo.findDriverByEmail(email).isPresent();
    }




}
