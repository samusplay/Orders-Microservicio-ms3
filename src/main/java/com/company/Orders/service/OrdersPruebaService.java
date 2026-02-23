package com.company.Orders.service;

import com.company.Orders.models.OrdersPruebaDTO;

public interface OrdersPruebaService {
    OrdersPruebaDTO create(OrdersPruebaDTO dto);
    OrdersPruebaDTO findById(Long id);
}
