package com.homebite.provider_service.Services;


import com.homebite.provider_service.DTOs.RequestDTO.ProviderDTO;
import com.homebite.provider_service.DTOs.ResponseDTO.ResponseDTO;
import com.homebite.provider_service.Entity.Provider;
import com.homebite.provider_service.Repositories.ProviderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProviderService {

   final private ProviderRepo providerRepo;
   final private PasswordEncoder passwordEncoder;

    @Autowired
    public ProviderService(ProviderRepo providerRepo, PasswordEncoder passwordEncoder){
        this.providerRepo=providerRepo;
        this.passwordEncoder= passwordEncoder;
    }


    public Provider register(ProviderDTO providerDTO){
        if(providerDTO.getPassword()==null || providerDTO.getPassword().isEmpty()){
            throw new IllegalArgumentException("Password cannot be null");

        }

        Provider provider = new Provider();

        provider.setUsername(providerDTO.getUsername());
        provider.setEmail(providerDTO.getEmail());
        provider.setPassword(passwordEncoder.encode(providerDTO.getPassword()));
        provider.setRest_name(providerDTO.getRest_name());
        provider.setRest_address(providerDTO.getRest_address());

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





}
