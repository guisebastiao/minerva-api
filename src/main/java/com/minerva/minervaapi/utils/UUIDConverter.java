package com.minerva.minervaapi.utils;

import com.minerva.minervaapi.exceptions.BadRequestException;

import java.util.UUID;

public class UUIDConverter {
    public static UUID toUUID(String id) {
        try {
            return UUID.fromString(id);
        } catch (Exception e) {
            throw new BadRequestException("O id está inválido");
        }
    }
}
