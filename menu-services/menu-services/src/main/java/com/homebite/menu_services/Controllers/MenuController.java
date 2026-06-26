package com.homebite.menu_services.Controllers;

import com.homebite.menu_services.Client.ProviderClient;
import com.homebite.menu_services.DTOs.RequestDTO.DishDTO;
import com.homebite.menu_services.Entity.Menu;
import com.homebite.menu_services.Services.MenuServices;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/menus")
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
            @ModelAttribute DishDTO dishDTO,
            @RequestParam("image") MultipartFile image) {
        try {
            // 1. Let OpenFeign pass the cookie to the Provider Service to resolve the providerId
            Long providerId = providerClient.getLoggedInProviderId();
            System.out.println("DEBUG: Menu Service received verified Provider ID via Feign -> " + providerId);

            // 2. Pass down to your service layer to add the dish and upload to MinIO
            Menu updatedMenu = menuService.addDishToMenu(providerId, dishDTO, image);
            return ResponseEntity.ok(updatedMenu);

        } catch (Exception e) {
            System.out.println("DEBUG: Exception thrown during processing -> " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(401).body("Unauthorized or Processing Error: " + e.getMessage());
        }
    }
}