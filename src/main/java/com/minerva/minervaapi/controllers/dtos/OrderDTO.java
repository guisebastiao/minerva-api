package com.minerva.minervaapi.controllers.dtos;

import java.util.List;

public record OrderDTO(String order) {
    public OrderDTO {
        if (order == null || isValid(order)) {
            order = "";
        }
    }

    private static boolean isValid(String order) {
        List<String> orderFields = List.of("assessment", "date");
        return orderFields.contains(order);
    }
}
