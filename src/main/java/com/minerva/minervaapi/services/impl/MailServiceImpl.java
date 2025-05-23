package com.minerva.minervaapi.services.impl;

import com.minerva.minervaapi.controllers.dtos.MailDTO;
import com.minerva.minervaapi.exceptions.ServerErrorException;
import com.minerva.minervaapi.services.MailService;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {

    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendEmail(MailDTO mailDTO) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(from);
            helper.setTo(mailDTO.to());
            helper.setSubject(mailDTO.subject());
            helper.setText(mailDTO.template(), true);

            mailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new ServerErrorException("Algo deu errado, tente novamente mais tarde");
        }
    }
}
