package com.chatapp.backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "chat")
public record ChatProperties(int maxMessageChars, int contextTokenBudget) {
}