package com.minerva.minervaapi.services;

import com.minerva.minervaapi.models.User;
import org.springframework.stereotype.Service;

@Service
public interface TokenService {

    String generateToken(User user);
    String validateToken(String token);
}
