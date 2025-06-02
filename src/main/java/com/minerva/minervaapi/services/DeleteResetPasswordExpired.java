package com.minerva.minervaapi.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public interface DeleteResetPasswordExpired {

    @Scheduled(fixedDelay = 1000 * 60)
    void deleteResetPasswordExpired();
}