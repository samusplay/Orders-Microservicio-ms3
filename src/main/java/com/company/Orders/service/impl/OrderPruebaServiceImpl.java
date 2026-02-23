package com.company.Orders.service.impl;

import com.company.Orders.entity.OrdersPrueba;
import com.company.Orders.models.OrdersPruebaDTO;
import com.company.Orders.repository.OrdersPruebaRepository;
import com.company.Orders.service.OrdersPruebaService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderPruebaServiceImpl implements OrdersPruebaService {
    private final OrdersPruebaRepository repository;
    @Override
    public OrdersPruebaDTO create(OrdersPruebaDTO dto) {
        OrdersPrueba entity = new OrdersPrueba();
        entity.setEstadoCompra(dto.getEstadoCompra());

        OrdersPrueba guardado = repository.save(entity);

        dto.setId(guardado.getId());
        return dto;
    }

    @Override
    public OrdersPruebaDTO findById(Long id) {
        OrdersPrueba entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró la orden con ID: " + id));

        OrdersPruebaDTO dto = new OrdersPruebaDTO();
        dto.setId(entity.getId());
        dto.setEstadoCompra(entity.getEstadoCompra());
        return dto;
    }
}
