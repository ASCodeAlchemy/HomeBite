package com.homebite.driver_service.Repositories;

import com.homebite.driver_service.Entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DriverRepo extends JpaRepository<Driver, UUID> {

    Optional<Driver> findByLicenceNo(long licenceNo);
    Optional<Driver> findDriverByEmail(String email);
}
