package com.minerva.minervaapi.controllers.dtos;

public record MailDTO(
        String to,
        String subject,
        String template
) { }
