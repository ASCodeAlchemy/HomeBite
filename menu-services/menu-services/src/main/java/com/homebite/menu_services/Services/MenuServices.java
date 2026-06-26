package com.homebite.menu_services.Services;


import com.homebite.menu_services.Client.ProviderClient;
import com.homebite.menu_services.DTOs.RequestDTO.DishDTO;
import com.homebite.menu_services.DTOs.RequestDTO.MenuDTO;
import com.homebite.menu_services.Entity.Dishes;
import com.homebite.menu_services.Entity.Menu;
import com.homebite.menu_services.Repositories.MenuRepo;
import org.springframework.stereotype.Service;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


@Service
public class MenuServices {


    private final MenuRepo menuRepository;
    private final  ProviderClient providerClient;
    private final MinioClient minioClient;


    @Autowired
    public MenuServices(MenuRepo menuRepository, ProviderClient providerClient, MinioClient minioClient) {
        this.menuRepository = menuRepository;
        this.providerClient = providerClient;
        this.minioClient = minioClient;
    }

    @Value("${minio.bucket-name}")
    private String bucketName;


    public Menu addDishToMenu(Long providerId, DishDTO dishDTO, MultipartFile image) throws Exception {


        Menu menu = menuRepository.findByProviderId(providerId).orElseGet(() -> {

            Map<String, Object> providerInfo = providerClient.getProviderInternalInfo(providerId);
            if (providerInfo == null || !(Boolean) providerInfo.get("isActive")) {
                throw new RuntimeException("Invalid or Inactive Provider");
            }
            Menu newMenu = new Menu();
            newMenu.setProviderId(providerId);
            newMenu.setRestaurantName((String) providerInfo.get("restaurantName"));
            return newMenu;
        });


        String fileName = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .stream(image.getInputStream(), image.getSize(), -1)
                        .contentType(image.getContentType())
                        .build()
        );


        Dishes dish = new Dishes();
        dish.setDishName(dishDTO.getDishName());
        dish.setPrice(dishDTO.getPrice());
        dish.setVeg(dishDTO.getVeg());
        dish.setImageFileName(fileName);

        menu.getDishes().add(dish);

        return menuRepository.save(menu);
    }


    public List<Menu> getDashboardMenus() {
        List<Menu> menus = menuRepository.findAll();

        // Loop through everything to attach temporary MinIO URLs dynamically before sending to UI
        for (Menu menu : menus) {
            for (Dishes dish : menu.getDishes()) {
                dish.setImageFileName(getPresignedUrl(dish.getImageFileName()));
            }
        }
        return menus;
    }

    private String getPresignedUrl(String fileName) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(fileName)
                            .expiry(2, TimeUnit.HOURS)
                            .build()
            );
        } catch (Exception e) {
            return "Error generating link";
        }
    }


}
