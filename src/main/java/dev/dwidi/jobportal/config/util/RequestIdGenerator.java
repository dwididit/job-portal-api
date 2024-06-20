package dev.dwidi.jobportal.config.util;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RequestIdGenerator {
    public static String generateRequestId() {
        return UUID.randomUUID().toString();
    }
}

