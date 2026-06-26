package com.homebite.provider_service.Services;


import com.homebite.provider_service.Config.JWTService;
import com.homebite.provider_service.DTOs.RequestDTO.ProviderDTO;
import com.homebite.provider_service.DTOs.ResponseDTO.ResponseDTO;
import com.homebite.provider_service.Entity.Provider;
import com.homebite.provider_service.Repositories.ProviderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@Service
public class ProviderService {

   final private ProviderRepo providerRepo;
   final private PasswordEncoder passwordEncoder;
   final private JWTService jwtService;



    @Autowired
    public ProviderService(ProviderRepo providerRepo, PasswordEncoder passwordEncoder,JWTService jwtService){
        this.providerRepo=providerRepo;
        this.passwordEncoder= passwordEncoder;
        this.jwtService=jwtService;


    }


    public Provider register(ProviderDTO providerDTO){
        if(providerDTO.getPassword()==null || providerDTO.getPassword().isEmpty()){
            throw new IllegalArgumentException("Password cannot be null");

        }

        Provider provider = new Provider();

        provider.setEmail(providerDTO.getEmail());
        provider.setPassword(passwordEncoder.encode(providerDTO.getPassword()));
        provider.setRestName(providerDTO.getRestName());
        provider.setRestAddress(providerDTO.getRestAddress());

        return provider;
    }

    public ResponseDTO signUp(ProviderDTO providerDTO){
        Optional<Provider> ProEmail = providerRepo.findByEmail(providerDTO.getEmail());

        if(ProEmail.isPresent()){
            throw new IllegalArgumentException("Email Already Registered");

        }

        Provider provider = register(providerDTO);
        providerRepo.save(provider);

        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage("User Registered Successfully");

        return responseDTO;

    }

    public boolean emailExists(String email){
        return providerRepo.findByEmail(email).isPresent();
    }

    @GetMapping("/me")
    public ResponseEntity<?> getLoggedInProviderId(HttpServletRequest request) {
        String token = null;

        // Fallback 1: Check standard servlet cookies array
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("jwt".equals(cookie.getName())) {
                    token = cookie.getValue();
                }
            }
        }

        // Fallback 2: Check raw Cookie header string (essential for custom Postman headers)
        if (token == null) {
            String rawCookie = request.getHeader("Cookie");
            if (rawCookie != null && rawCookie.contains("jwt=")) {
                // Extracts the token value from "jwt=ey..."
                token = rawCookie.split("jwt=")[1].split(";")[0];
            }
        }

        if (token == null) {
            System.out.println("PROVIDER-SERVICE DEBUG: Token not found in cookies or headers!");
            return ResponseEntity.status(401).body("Token cookie missing");
        }

        try {

            Long providerId = Long.valueOf(jwtService.extractUsername(token));

            System.out.println("PROVIDER-SERVICE DEBUG: Token decoded successfully for ID -> " + providerId);
            return ResponseEntity.ok(providerId);

        } catch (Exception e) {
            System.out.println("PROVIDER-SERVICE DEBUG: JWT Parsing Failed -> " + e.getMessage());
            return ResponseEntity.status(401).body("Invalid Token Signature or Expired");
        }
    }






    





}
