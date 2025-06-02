package com.minerva.minervaapi.services.impl;

import com.minerva.minervaapi.repositories.ResetPasswordRepository;
import com.minerva.minervaapi.services.DeleteResetPasswordExpired;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class DeleteResetPasswordExpiredImpl implements DeleteResetPasswordExpired {

    @Autowired
    private ResetPasswordRepository resetPasswordRepository;

    @Transactional
    public void deleteResetPasswordExpired() {
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        resetPasswordRepository.deleteAllByExpiryDateBefore(now);
    }
}