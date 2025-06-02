package com.minerva.minervaapi.services.impl;

import com.minerva.minervaapi.controllers.dtos.*;
import com.minerva.minervaapi.exceptions.BadRequestException;
import com.minerva.minervaapi.exceptions.EntityNotFoundException;
import com.minerva.minervaapi.models.ResetPassword;
import com.minerva.minervaapi.models.User;
import com.minerva.minervaapi.repositories.ResetPasswordRepository;
import com.minerva.minervaapi.repositories.UserRepository;
import com.minerva.minervaapi.services.MailService;
import com.minerva.minervaapi.services.RabbitMailService;
import com.minerva.minervaapi.services.ResetPasswordService;
import com.minerva.minervaapi.utils.UUIDConverter;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Service
public class ResetPasswordServiceImpl implements ResetPasswordService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ResetPasswordRepository resetPasswordRepository;

    @Autowired
    private RabbitMailService rabbitMailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TemplateEngine templateEngine;


    @Value("${frontend.url}")
    private String frontendUrl;

    @Override
    @Transactional
    public DefaultDTO createResetPassword(CreateResetPasswordDTO createResetPasswordDTO) {
        User user = this.userRepository.findByEmail(createResetPasswordDTO.email())
                .orElseThrow(() -> new EntityNotFoundException("Esse e-mail não está cadastrado"));

        this.resetPasswordRepository.deleteByUserId(user.getId());

        ResetPassword resetPassword = new ResetPassword();
        resetPassword.setToken(UUID.randomUUID());
        resetPassword.setUser(user);
        resetPassword.setExpiryDate(this.getExpiryDate());

        ResetPassword resetPasswordSaved = this.resetPasswordRepository.save(resetPassword);

        String link = this.generateLink(resetPasswordSaved.getToken());
        String template = this.templateMail(link);

        String subject = "Recuperar Senha - Minerva";
        MailDTO mailDTO = new MailDTO(user.getEmail(), subject, template);

        RabbitMailDTO rabbitMailDTO = new RabbitMailDTO(mailDTO);

        rabbitMailService.producer(rabbitMailDTO);

        return new DefaultDTO("Você recebeu um email para redefinir sua senha", Boolean.TRUE, null, null, null);
    }

    @Override
    @Transactional
    public DefaultDTO resetPassword(String token, ResetPasswordDTO resetPasswordDTO) {
        ResetPassword resetPassword = this.resetPasswordRepository.findByToken(UUIDConverter.toUUID(token))
                .orElseThrow(() -> new BadRequestException("A recuperação da sua senha se expirou ou ela já foi alterada"));

        if (!resetPasswordDTO.newPassword().equals(resetPasswordDTO.confirmPassword())) {
            throw new BadRequestException("As senhas não se coincidem");
        }

        User user = resetPassword.getUser();
        user.setPassword(this.passwordEncoder.encode(resetPasswordDTO.newPassword()));
        user.setResetPassword(null);

        this.userRepository.save(user);

        this.resetPasswordRepository.delete(resetPassword);

        return new DefaultDTO("Sua senha foi recuperada com sucesso", Boolean.TRUE, null, null, null);
    }

    private LocalDateTime getExpiryDate() {
        return LocalDateTime.now(ZoneOffset.UTC).plusMinutes(5);
    }

    private String templateMail(String link) {
        Context context = new Context();
        context.setVariable("recoveryUrl", link);
        return templateEngine.process("recover-password", context);
    }

    private String generateLink(UUID token) {
        return String.format(this.frontendUrl + "/forgot-password/" + token);
    }
}
