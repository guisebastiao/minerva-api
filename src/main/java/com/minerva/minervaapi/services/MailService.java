package com.minerva.minervaapi.services;

import com.minerva.minervaapi.controllers.dtos.MailDTO;

public interface MailService {
    void sendEmail(MailDTO mailDTO);
}
