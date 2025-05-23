package com.minerva.minervaapi.services;

import com.minerva.minervaapi.controllers.dtos.TokenResponseDTO;
import com.minerva.minervaapi.models.User;

public interface TokenService {

    TokenResponseDTO generateToken(User user);
    String validateToken(String token);
}
