package com.homebite.order_service.Repositories;


import com.homebite.order_service.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepo extends JpaRepository<Order,String> {
    List<Order> findByUserIdOrderByCreatedAtDesc(String userId);
    List<Order> findByProviderIdOrderByCreatedAtDesc(String providerId);
}
