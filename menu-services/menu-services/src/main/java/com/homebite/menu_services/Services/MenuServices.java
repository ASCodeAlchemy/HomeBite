package com.homebite.menu_services.Services;


import com.homebite.menu_services.Client.ProviderClient;
import com.homebite.menu_services.DTOs.RequestDTO.TiffinDTO;
import com.homebite.menu_services.DTOs.ResponseDTO.TiffinDetailsDTO;
import com.homebite.menu_services.Entity.Menu;
import com.homebite.menu_services.Entity.Tiffin;
import com.homebite.menu_services.Repositories.MenuRepo;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
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


    public Menu addDishToMenu(Long providerId, TiffinDTO dishDTO, MultipartFile image, String token, String email) throws Exception {

        Menu menu = menuRepository.findByProviderId(providerId).orElseGet(() -> {
            Map<String, Object> providerInfo = providerClient.getProviderInternalInfo(providerId,token,email);
            if (providerInfo == null) {
                throw new RuntimeException("Provider info could not be retrieved from internal client");
            }

            Menu newMenu = new Menu();
            newMenu.setProviderId(providerId);

            newMenu.setRestaurantName((String) providerInfo.get("restName"));
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


        Tiffin dish = new Tiffin();
        dish.setDishName(dishDTO.getDishName());
        dish.setPrice(dishDTO.getPrice());
        dish.setVeg(dishDTO.getVeg());
        dish.setImageFileName(fileName);



        menu.getDishes().add(dish);

        return menuRepository.save(menu);
    }


    public List<Menu> getDashboardMenus() {
        List<Menu> menus = menuRepository.findAll();

        for (Menu menu : menus) {
            for (Tiffin dish : menu.getDishes()) {
                dish.setImageUrl(getPresignedUrl(dish.getImageFileName()));
            }
        }
        return menus;
    }

    public TiffinDetailsDTO getTiffinDetails(Long tiffinId) {
        Menu menu = menuRepository.findMenuContainingDish(tiffinId)
                .orElseThrow(() -> new IllegalArgumentException("Tiffin not found"));
        Tiffin tiffin = menu.getDishes().stream()
                .filter(dish -> tiffinId.equals(dish.getTiffinId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Tiffin not found"));

        return new TiffinDetailsDTO(
                tiffin.getTiffinId(),
                menu.getProviderId(),
                tiffin.getDishName(),
                BigDecimal.valueOf(tiffin.getPrice()),
                tiffin.getVeg()
        );
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
