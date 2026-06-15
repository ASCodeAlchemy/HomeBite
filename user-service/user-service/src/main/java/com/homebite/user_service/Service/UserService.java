package com.homebite.user_service.Service;


import com.homebite.user_service.DTOs.RequestDTO.UserDTO;
import com.homebite.user_service.DTOs.ResponseDTO.ResponseDTO;
import com.homebite.user_service.Entitiy.Users;
import com.homebite.user_service.Repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public Users register(UserDTO userDTO){
        if(userDTO.getPassword()==null || userDTO.getPassword().isEmpty()){
            throw new IllegalArgumentException("Password cannot be null");
        }

        Users user = new Users();

        user.setUsername(userDTO.getUsername());
       user.setEmail(userDTO.getEmail());
       user.setFullname(userDTO.getFullname());
       user.setAddress(userDTO.getAddress());
       user.setPassword(passwordEncoder.encode(userDTO.getPassword()));



        return user;

    }

    public ResponseDTO signUp(UserDTO userDTO) {
        Optional<Users> userEmail = userRepo.findByEmail(userDTO.getEmail());
        if (userEmail.isPresent()) {
            throw new IllegalStateException("Email is Already Registered");

        }
        Users user = register(userDTO);
        userRepo.save(user);
        ResponseDTO dto = new ResponseDTO();
        dto.setMessage("User Registered Successfully");
        return dto;

    }

    public boolean emailExists(String email) {
        return userRepo.findByEmail(email).isPresent();
    }



}
