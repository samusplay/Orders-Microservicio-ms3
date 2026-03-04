package com.company.Orders.repository;

import com.company.Orders.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
    //metodos personalizados que se requieran

}
