package com.minerva.minervaapi.controllers.dtos;

import java.time.Instant;

public record TokenResponseDTO(
    String token,
    Instant expires
) { }
