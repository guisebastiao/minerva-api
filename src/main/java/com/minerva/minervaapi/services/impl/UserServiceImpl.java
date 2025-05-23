package com.minerva.minervaapi.services.impl;

import com.minerva.minervaapi.controllers.dtos.DefaultDTO;
import com.minerva.minervaapi.controllers.dtos.UserResetPasswordDTO;
import com.minerva.minervaapi.controllers.dtos.UserResponseDTO;
import com.minerva.minervaapi.controllers.dtos.UserUpdateDTO;
import com.minerva.minervaapi.controllers.mappers.UserMapper;
import com.minerva.minervaapi.exceptions.BadRequestException;
import com.minerva.minervaapi.exceptions.EntityNotFoundException;
import com.minerva.minervaapi.exceptions.UnauthorizedException;
import com.minerva.minervaapi.models.User;
import com.minerva.minervaapi.repositories.UserRepository;
import com.minerva.minervaapi.security.AuthProvider;
import com.minerva.minervaapi.services.UserService;
import com.minerva.minervaapi.utils.UUIDConverter;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AuthProvider authProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public DefaultDTO findUserById(String userId) {
        User user = this.userRepository.findById(UUIDConverter.toUUID(userId))
                .orElseThrow(() -> new EntityNotFoundException("Usuário não foi encontrado"));

        UserResponseDTO userResponseDTO = this.userMapper.toDTO(user);
        return new DefaultDTO("Usuário encontrado com sucesso", Boolean.TRUE, userResponseDTO, null, null);
    }

    @Override
    @Transactional
    public DefaultDTO resetPasswordAccount(UserResetPasswordDTO userResetPasswordDTO) {
        User user = this.getAuthenticatedUser();

        if(!passwordEncoder.matches(userResetPasswordDTO.currentPassword(), user.getPassword())) {
            throw new UnauthorizedException("Sua senha atual está incorreta");
        }

        if (!userResetPasswordDTO.newPassword().equals(userResetPasswordDTO.confirmPassword())) {
            throw new BadRequestException("As senhas não se coincidem");
        }

        user.setPassword(passwordEncoder.encode(userResetPasswordDTO.newPassword()));

        this.userRepository.save(user);

        return new DefaultDTO("Sua senha foi atualizada com sucesso", Boolean.TRUE, null, null, null);
    }

    @Override
    @Transactional
    public DefaultDTO updateAccount(UserUpdateDTO userUpdateDTO) {
        User user = this.getAuthenticatedUser();

        user.setName(userUpdateDTO.name());

        this.userRepository.save(user);

        return new DefaultDTO("Sua conta foi atualizada com sucesso", Boolean.TRUE, null, null, null);
    }

    @Override
    @Transactional
    public DefaultDTO deleteAccount() {
        User user = this.getAuthenticatedUser();

        this.userRepository.delete(user);

        return new DefaultDTO("Sua conta foi deletada com sucesso", Boolean.TRUE, null, null, null);
    }

    private User getAuthenticatedUser() {
        return authProvider.getAuthenticatedUser();
    }
}
