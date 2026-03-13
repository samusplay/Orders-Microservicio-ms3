package com.company.Orders.models;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import jakarta.validation.constraints.NotNull;

import java.util.List;


@Data
public class CreateOrderRequestDTO {

    @NotEmpty(message = "La orden debe contener al menos un producto")
    @Valid
    private List<ItemRequestDTO> items;



}
