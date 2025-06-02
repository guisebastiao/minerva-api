package com.minerva.minervaapi.services.impl;

import com.minerva.minervaapi.config.RabbitMQConfig;
import com.minerva.minervaapi.controllers.dtos.RabbitMailDTO;
import com.minerva.minervaapi.services.MailService;
import com.minerva.minervaapi.services.RabbitMailService;
import jakarta.transaction.Transactional;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMailServiceImpl implements RabbitMailService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private MailService mailService;

    @Override
    @Transactional
    public void consumer(RabbitMailDTO emailConsumerDTO) {
        mailService.sendEmail(emailConsumerDTO.mailDTO());
    }

    @Override
    @Transactional
    public void producer(RabbitMailDTO emailConsumerDTO) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_NAME, emailConsumerDTO);
    }
}
