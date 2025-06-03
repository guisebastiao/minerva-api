package com.minerva.minervaapi.services;

import com.minerva.minervaapi.controllers.dtos.RabbitMailDTO;

public interface RabbitMailService {
    void producer(RabbitMailDTO emailConsumerDTO);
}
