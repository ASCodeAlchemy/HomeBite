package com.homebite.provider_service.Repositories;

import com.homebite.provider_service.Entity.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProviderRepo extends JpaRepository<Provider,Long> {

    Optional<Provider> findByEmail(String email);
}
