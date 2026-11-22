package com.chatapp.backend.session.dto;

import java.time.Instant;
import java.util.UUID;

import com.chatapp.backend.session.ChatMessage;

public record MessageResponse(UUID id, ChatMessage.Role role, String content,
                              ChatMessage.Status status, Instant createdAt) {

    public static MessageResponse from(ChatMessage m) {
        return new MessageResponse(m.getId(), m.getRole(), m.getContent(), m.getStatus(), m.getCreatedAt());
    }
}