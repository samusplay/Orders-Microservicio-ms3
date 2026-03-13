package com.company.Orders.entity;

import com.company.Orders.models.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    //enum para manejar el estado
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    //una orden tiene muchos items
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OrderItem> items = new ArrayList<>();

    //mantenga la relacion
    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }



}
