package com.homebite.menu_services.Repositories;

import com.homebite.menu_services.Entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MenuRepo extends JpaRepository<Menu, Integer> {

    Optional<Menu> findByProviderId(Long providerId);

    @Query("""
    select distinct m
    from Menu m
    join fetch m.dishes d
    where d.tiffinId = :dishId
""")
    Optional<Menu> findMenuContainingDish(@Param("dishId") Long dishId);
}
