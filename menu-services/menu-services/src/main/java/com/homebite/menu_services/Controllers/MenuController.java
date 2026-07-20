package com.homebite.menu_services.Controllers;

import com.homebite.menu_services.Client.ProviderClient;
import com.homebite.menu_services.DTOs.RequestDTO.DishDTO;
import com.homebite.menu_services.Entity.Menu;
import com.homebite.menu_services.Services.MenuServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class MenuController {

    private final MenuServices menuService;
    private final ProviderClient providerClient;

    @Autowired
    public MenuController(MenuServices menuService, ProviderClient providerClient) {
        this.menuService = menuService;
        this.providerClient = providerClient;
    }

    @PostMapping(value = "/add-dish", consumes = {"multipart/form-data"})
    public ResponseEntity<?> addDishToMenu(
            @RequestHeader("Authorization") String token,
            @RequestHeader("X-User-Email") String email,
            @ModelAttribute DishDTO dishDTO,
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
}


