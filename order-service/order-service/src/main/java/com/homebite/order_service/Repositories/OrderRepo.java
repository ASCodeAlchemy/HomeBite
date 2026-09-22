package com.homebite.order_service.Repositories;


import com.homebite.order_service.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepo extends JpaRepository<Order,String> {
    List<Order> findByUserIdOrderByCreatedAtDesc(String userId);
    List<Order> findByProviderIdOrderByCreatedAtDesc(String providerId);
    List<Order> findTop10ByUserIdOrderByCreatedAtDesc(String userId);


    List<Order> findByProviderId(String providerId);
}
