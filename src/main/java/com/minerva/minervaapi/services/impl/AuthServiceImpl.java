package com.minerva.minervaapi.services.impl;

import com.minerva.minervaapi.controllers.dtos.*;
import com.minerva.minervaapi.controllers.mappers.UserMapper;
import com.minerva.minervaapi.exceptions.DuplicateEntityException;
import com.minerva.minervaapi.exceptions.EntityNotFoundException;
import com.minerva.minervaapi.exceptions.UnauthorizedException;
import com.minerva.minervaapi.models.User;
import com.minerva.minervaapi.repositories.UserRepository;
import com.minerva.minervaapi.services.AuthService;
import com.minerva.minervaapi.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional
    public DefaultDTO login(LoginDTO loginDTO) {
        User user = this.findUserByEmail(loginDTO.email());

        if(user == null) {
            throw new EntityNotFoundException("Essa conta não esta cadastrada");
        }

        if(!passwordEncoder.matches(loginDTO.password(), user.getPassword())) {
            throw new UnauthorizedException("Senha incorreta");
        }

        TokenResponseDTO tokenResponseDTO = this.tokenService.generateToken(user);
        UserResponseDTO userResponseDTO = this.userMapper.toDTO(user);
        AuthResponseDTO authResponseDTO = new AuthResponseDTO(tokenResponseDTO.token(), tokenResponseDTO.expires(), userResponseDTO);
        return new DefaultDTO("Login efetuado com sucesso", Boolean.TRUE, authResponseDTO, null, null);
    }

    @Override
    @Transactional
    public DefaultDTO register(RegisterDTO registerDTO) {
        User user = this.findUserByEmail(registerDTO.email());

        if(user != null) {
            throw new DuplicateEntityException("Essa conta já está cadastrada");
        }

        User saveUser = this.userMapper.toUser(registerDTO);
        saveUser.setPassword(this.passwordEncoder.encode(registerDTO.password()));

        this.userRepository.save(saveUser);

        return new DefaultDTO("Sua conta foi cadastrada com sucesso", Boolean.TRUE, null, null, null);
    }

    private User findUserByEmail(String email) {
        return this.userRepository.findByEmail(email).orElse(null);
    }
}
