package com.homebite.provider_service.Controller;

import com.homebite.provider_service.Config.JWTService;
import com.homebite.provider_service.Config.MyUserDetailService;
import com.homebite.provider_service.DTOs.RequestDTO.OtpDto;
import com.homebite.provider_service.DTOs.RequestDTO.ProviderDTO;
import com.homebite.provider_service.DTOs.ResponseDTO.ResponseDTO;
import com.homebite.provider_service.Repositories.ProviderRepo;
import com.homebite.provider_service.Services.EmailService;
import com.homebite.provider_service.Services.OTPService;
import com.homebite.provider_service.Services.PendingUserService;
import com.homebite.provider_service.Services.ProviderService;
import com.netflix.discovery.converters.Auto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ProviderController {

    private final ProviderService providerService;
    private final JWTService jwtService;
    private final MyUserDetailService myUserDetailService;
    private final OTPService otpService;
    private final EmailService emailService;
    private final PendingUserService pendingUserService;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ProviderRepo providerRepo;


    @Autowired
    public ProviderController(ProviderService providerService, JWTService jwtService, MyUserDetailService myUserDetailService, OTPService otpService, EmailService emailService, PendingUserService pendingUserService, KafkaTemplate<String, Object> kafkaTemplate, ProviderRepo providerRepo) {
        this.providerService = providerService;
        this.jwtService = jwtService;
        this.myUserDetailService = myUserDetailService;
        this.otpService = otpService;
        this.emailService = emailService;
        this.pendingUserService = pendingUserService;
        this.kafkaTemplate = kafkaTemplate;
        this.providerRepo = providerRepo;
    }


    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> registerProvider(@RequestBody ProviderDTO providerDTO) throws Exception{

        if(providerDTO.getPassword() == null ||providerDTO.getPassword().isEmpty()){
            throw new IllegalArgumentException("Password Cannot be null");

        }

        if(providerService.emailExists(providerDTO.getEmail())){
            throw new IllegalArgumentException("Email Already Exists");
        }

        pendingUserService.savePendingUser(providerDTO);
        String otp = otpService.generateOtp(providerDTO.getEmail());

        emailService.sendVerificationEmail(providerDTO.getEmail(),otp,"REGISTER_OTP");

        return ResponseEntity.ok(new ResponseDTO("OTP Send to "+ providerDTO.getEmail()));

    }

    @PostMapping("/verify-register-otp")
    public ResponseEntity<ResponseDTO> verifyRegisterOtp(@RequestBody OtpDto otpDTO) throws Exception {
        String otp = otpDTO.getOtp();
        String email = otpService.getEmailByOtp(otp);

        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDTO("Invalid or expired OTP"));
        }

        ProviderDTO pendingUser = pendingUserService.getPendingUser(email);
        if (pendingUser == null) {
            return ResponseEntity.badRequest().body(new ResponseDTO("Registration session expired"));
        }

        providerService.signUp(pendingUser);
        pendingUserService.remove(email);
        otpService.removeOtp(otp);

        Map<String, Object> welcomeEvent = new HashMap<>();
        welcomeEvent.put("email", email);

        welcomeEvent.put("type", "REGISTER_SUCCESS");
        kafkaTemplate.send("auth-events", email, welcomeEvent);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO("User registered successfully"));
    }


    @PostMapping("/auth/login")
    public ResponseEntity<ResponseDTO> login(@RequestBody ProviderDTO providerDTO){
        if(!providerService.emailExists(providerDTO.getEmail())){
            throw new IllegalArgumentException("Email is not Registered");
        }

        String ProEmail = providerDTO.getEmail();

        String otp = otpService.generateOtp(ProEmail);
        emailService.sendVerificationEmail(ProEmail,otp,"LOGIN_OTP");

        return ResponseEntity.ok(new ResponseDTO("OTP Sent to "+ProEmail));
    }


    @PostMapping("/verify-otp")
    public ResponseEntity<ResponseDTO> verifyLogin(@RequestBody OtpDto otpDto, HttpServletResponse response){
        String otp = otpDto.getOtp();

        String email = otpService.verifyOtp(otp);


        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDTO("Invalid or expired OTP"));
        }


        UserDetails userDetails = myUserDetailService.loadUserByUsername(email);
        String jwt = jwtService.generateToken(userDetails);

        Cookie cookie = new Cookie("jwt", jwt);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24);
        response.addCookie(cookie);

        return ResponseEntity.ok(new ResponseDTO("Login successful"));
    }
    }


