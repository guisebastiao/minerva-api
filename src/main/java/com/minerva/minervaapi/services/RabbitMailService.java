package com.minerva.minervaapi.services;

import com.minerva.minervaapi.config.RabbitMQConfig;
import com.minerva.minervaapi.controllers.dtos.RabbitMailDTO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

public interface RabbitMailService {
    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    void consumer(RabbitMailDTO emailConsumerDTO);
    void producer(RabbitMailDTO emailConsumerDTO);
}
