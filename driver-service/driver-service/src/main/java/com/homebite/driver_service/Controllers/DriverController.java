package com.homebite.driver_service.Controllers;


import com.homebite.driver_service.Config.JWTService;
import com.homebite.driver_service.Config.MyUserDetailService;
import com.homebite.driver_service.DTOs.RequestDTO.DriverDTO;
import com.homebite.driver_service.DTOs.RequestDTO.OtpDTO;
import com.homebite.driver_service.DTOs.ResponseDTO.ResponseDTO;
import com.homebite.driver_service.Services.DriverService;
import com.homebite.driver_service.Services.EmailService;
import com.homebite.driver_service.Services.OTPService;
import com.homebite.driver_service.Services.PendingUserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class DriverController {

    private final DriverService driverService;
    private final JWTService jwtService;
    private final MyUserDetailService userDetailService;
    private final OTPService otpService;
    private final EmailService emailService;
    private final PendingUserService pendingUserService;
    private final KafkaTemplate<String,Object> kafkaTemplate;


    @Autowired
    public DriverController(DriverService driverService, JWTService jwtService, MyUserDetailService userDetailService, OTPService otpService, EmailService emailService, PendingUserService pendingUserService, KafkaTemplate<String, Object> kafkaTemplate) {
        this.driverService = driverService;
        this.jwtService = jwtService;
        this.userDetailService = userDetailService;
        this.otpService = otpService;
        this.emailService = emailService;
        this.pendingUserService = pendingUserService;
        this.kafkaTemplate = kafkaTemplate;
    }

    public ResponseEntity<ResponseDTO> register(@RequestBody DriverDTO driverDTO) throws Exception{
        if(driverDTO.getPassword()==null || driverDTO.getPassword().isEmpty()){
            throw new IllegalArgumentException("Password Cannot be Null");

        }

        if(driverService.emailExists(driverDTO.getEmail())){
            return ResponseEntity.badRequest().body(new ResponseDTO("Email Already Exists"));
        }

        pendingUserService.savePendingUser(driverDTO);
        String otp = otpService.generateOtp(driverDTO.getEmail());
        emailService.sendVerificationEmail(driverDTO.getEmail(),otp,"REGISTER_OTP");
        return ResponseEntity.ok().body(new ResponseDTO("Verification OTP sent to your email"));

    }

    @PostMapping("/verify-register-otp")
    public ResponseEntity<ResponseDTO> verifyRegisterOtp(@RequestBody OtpDTO otpDTO) throws Exception {
        String otp = otpDTO.getOtp();
        String email = otpService.getEmailByOtp(otp);

        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDTO("Invalid or expired OTP"));
        }

        DriverDTO pendingUser = pendingUserService.getPendingUser(email);
        if (pendingUser == null) {
            return ResponseEntity.badRequest().body(new ResponseDTO("Registration session expired"));
        }

        driverService.signup(pendingUser);
        pendingUserService.remove(email);
        otpService.removeOtp(otp);

        Map<String, Object> welcomeEvent = new HashMap<>();
        welcomeEvent.put("email", email);

        welcomeEvent.put("type", "REGISTER_SUCCESS");
        kafkaTemplate.send("auth-events", email, welcomeEvent);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO("User registered successfully"));
    }



    public ResponseEntity<ResponseDTO> login(@RequestBody DriverDTO driverDTO){
        if(!driverService.emailExists(driverDTO.getEmail())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDTO("Invalid email or password"));
        }

        String otp = otpService.generateOtp(driverDTO.getEmail());
        emailService.sendVerificationEmail(driverDTO.getEmail(), otp, "LOGIN_OTP");
        return ResponseEntity.ok().body(new ResponseDTO("Login OTP sent to your email"));
    }


    public ResponseEntity<ResponseDTO> verifyLoginOtp(@RequestBody OtpDTO otpDTO, HttpServletResponse response) {
        String otp = otpDTO.getOtp();

        String email = otpService.verifyOtpAndGetEmail(otp);

        if(email == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDTO("Invalid or expired OTP"));
        }

        UserDetails userDetails = userDetailService.loadUserByUsername(email);
        String jwt = jwtService.generateToken(userDetails);

        Cookie cookie = new Cookie("jwt",jwt);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(60*60*24);
        response.addCookie(cookie);
        return ResponseEntity.ok().body(new ResponseDTO("Login successful"));
    }

}
