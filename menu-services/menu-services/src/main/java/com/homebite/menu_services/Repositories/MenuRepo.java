package com.homebite.menu_services.Repositories;

import com.homebite.menu_services.Entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MenuRepo extends JpaRepository<Menu, Integer> {

    Optional<Menu> findByProviderId(Long providerId);
}
