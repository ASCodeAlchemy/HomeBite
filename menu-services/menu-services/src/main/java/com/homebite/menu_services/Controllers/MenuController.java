package com.homebite.menu_services.Controllers;

import com.homebite.menu_services.Client.ProviderClient;
import com.homebite.menu_services.DTOs.RequestDTO.TiffinDTO;
import com.homebite.menu_services.DTOs.ResponseDTO.TiffinDetailsDTO;
import com.homebite.menu_services.Entity.Menu;
import com.homebite.menu_services.Services.MenuServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class MenuController {

    private final MenuServices menuService;
    private final ProviderClient providerClient;
    private final String internalServiceSecret;

    @Autowired
    public MenuController(MenuServices menuService,
                          ProviderClient providerClient,
                          @Value("${internal.service-secret}") String internalServiceSecret) {
        this.menuService = menuService;
        this.providerClient = providerClient;
        this.internalServiceSecret = internalServiceSecret;
    }

    @PostMapping(value = "/add-dish", consumes = {"multipart/form-data"})
    public ResponseEntity<?> addDishToMenu(
            @RequestHeader("Authorization") String token,
            @RequestHeader("X-User-Email") String email,
            @ModelAttribute TiffinDTO dishDTO,
            @RequestParam("image") MultipartFile image) {


        try {


            Long providerId = providerClient.getLoggedInProviderId(token, email);

            System.out.println("DEBUG: Menu Service received verified Provider ID via Feign -> " + providerId);

            if (providerId == null || providerId == 0) {
                return ResponseEntity.status(403).body("Access Denied: This account is not registered as a Provider.");
            }

            Menu updatedMenu = menuService.addDishToMenu(providerId, dishDTO, image, token, email);
            return ResponseEntity.ok(updatedMenu);

        } catch (Exception e) {
            System.out.println("DEBUG: Exception thrown during processing -> " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Processing Error: " + e.getMessage());
        }
    }

    @GetMapping("/menus/dashboard")
    public ResponseEntity<List<Menu>> getDashboardMenus() {
        return ResponseEntity.ok(menuService.getDashboardMenus());
    }

    @GetMapping("/internal/tiffins/{tiffinId}")
    public ResponseEntity<?> getTiffinDetails(@PathVariable Long tiffinId,
                                               @RequestHeader(value = "X-Internal-Service-Secret", required = false) String requestSecret) {
        if (!internalServiceSecret.equals(requestSecret)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Internal service access denied");
        }
        try {
            TiffinDetailsDTO tiffin = menuService.getTiffinDetails(tiffinId);
            return ResponseEntity.ok(tiffin);
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
    }
}


