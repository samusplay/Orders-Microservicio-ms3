package com.company.Orders.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "orders_prueba")
@Data
public class OrdersPrueba {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String estadoCompra; // Ej: "PENDIENTE", "COMPLETADA"
}
