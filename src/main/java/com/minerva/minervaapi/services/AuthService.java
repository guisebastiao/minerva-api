package com.minerva.minervaapi.services;

import com.minerva.minervaapi.controllers.dtos.DefaultDTO;
import com.minerva.minervaapi.controllers.dtos.LoginDTO;
import com.minerva.minervaapi.controllers.dtos.RegisterDTO;

public interface AuthService {
    DefaultDTO login(LoginDTO loginDTO);
    DefaultDTO register(RegisterDTO registerDTO);
}
