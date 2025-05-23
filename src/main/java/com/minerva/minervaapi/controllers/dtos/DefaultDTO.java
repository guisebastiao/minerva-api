package com.minerva.minervaapi.controllers.dtos;

import java.util.List;

public record DefaultDTO(
        String message,
        Boolean success,
        Object data,
        PagingDTO paging,
        List<FieldErrorDTO> fieldErrors
) {
}
