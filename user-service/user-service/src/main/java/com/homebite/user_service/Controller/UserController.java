package com.homebite.user_service.Controller;


import com.homebite.user_service.Config.JWTService;
import com.homebite.user_service.Config.MyUserDetailService;
import com.homebite.user_service.DTOs.RequestDTO.OtpDTO;
import com.homebite.user_service.DTOs.RequestDTO.UserDTO;
import com.homebite.user_service.DTOs.ResponseDTO.ResponseDTO;
import com.homebite.user_service.Service.EmailService;
import com.homebite.user_service.Service.OTPService;
import com.homebite.user_service.Service.PendingUserService;
import com.homebite.user_service.Service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {
    private final UserService userService;
    private final JWTService jwtService;
    private final MyUserDetailService myUserDetailService;
    private final OTPService otpService;
    private final EmailService emailService;
    private final PendingUserService pendingUserService;
    private final KafkaTemplate<String, Object> kafkaTemplate;



    @Autowired
    public UserController(UserService userService, JWTService jwtService, MyUserDetailService myUserDetailService, OTPService otpService, EmailService emailService, PendingUserService pendingUserService, KafkaTemplate<String, Object> kafkaTemplate) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.myUserDetailService = myUserDetailService;
        this.otpService = otpService;
        this.emailService = emailService;
        this.pendingUserService = pendingUserService;
        this.kafkaTemplate = kafkaTemplate;

    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> register(@RequestBody UserDTO userDTO) throws Exception {
        if (userDTO.getPassword() == null || userDTO.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        if (userService.emailExists(userDTO.getEmail())) {
            return ResponseEntity.badRequest().body(new ResponseDTO("Email already registered"));
        }
        pendingUserService.savePendingUser(userDTO);
        String otp = otpService.generateOtp(userDTO.getEmail());
        emailService.sendVerificationEmail(userDTO.getEmail(), otp, "REGISTER_OTP");

        return ResponseEntity.ok(new ResponseDTO("OTP sent to email for verification"));
    }

    @PostMapping("/verify-register-otp")
    public ResponseEntity<ResponseDTO> verifyRegisterOtp(@RequestBody OtpDTO otpDTO) throws Exception {
        String otp = otpDTO.getOtp();
        String email = otpService.getEmailByOtp(otp);

        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDTO("Invalid or expired OTP"));
        }

        UserDTO pendingUser = pendingUserService.getPendingUser(email);
        if (pendingUser == null) {
            return ResponseEntity.badRequest().body(new ResponseDTO("Registration session expired"));
        }

        userService.signUp(pendingUser);
        pendingUserService.remove(email);
        otpService.removeOtp(otp);

        Map<String, Object> welcomeEvent = new HashMap<>();
        welcomeEvent.put("email", email);

        welcomeEvent.put("type", "REGISTER_SUCCESS");
        kafkaTemplate.send("auth-events", email, welcomeEvent);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO("User registered successfully"));
    }


    @PostMapping("/auth/login")
public ResponseEntity<ResponseDTO> login(@RequestBody UserDTO userDTO) {
    if(!userService.emailExists(userDTO.getEmail())){
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDTO("Email Not Registered"));

    }

String otp = otpService.generateOtp(userDTO.getEmail());
emailService.sendVerificationEmail(userDTO.getEmail(),otp,"LOGIN_OTP");
return ResponseEntity.ok(new ResponseDTO("OTP Send to Email"));

}

    @PostMapping("/verify-otp")
    public ResponseEntity<ResponseDTO> verifyOtp(@RequestBody OtpDTO otpDTO, HttpServletResponse response) {
        String otp = otpDTO.getOtp();


        String email = otpService.verifyOtpAndGetEmail(otp);
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


    @GetMapping("/test")
    public String testRoute(@AuthenticationPrincipal UserDetails userDetails){
        if(userDetails==null){
            return "Check Ur Route it is Unauthorized";
        }

        return "Successful";
    }



}
