package com.minerva.minervaapi.controllers.dtos;

import jakarta.validation.constraints.NotNull;

public record CollectionFavoriteDTO(
        @NotNull(message = "Informe se a coleção é favorita")
        Boolean favorite
) { }
