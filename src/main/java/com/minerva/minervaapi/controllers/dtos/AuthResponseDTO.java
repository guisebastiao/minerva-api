package com.minerva.minervaapi.controllers.dtos;

import java.time.Instant;

public record AuthResponseDTO(
        String token,
        Instant expires,
        UserResponseDTO user
){ }
