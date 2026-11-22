package com.chatapp.backend.session.dto;

import java.time.Instant;
import java.util.UUID;

import com.chatapp.backend.session.ChatSession;

public record SessionResponse(UUID id, String title, Instant createdAt, Instant updatedAt) {

    public static SessionResponse from(ChatSession s) {
        return new SessionResponse(s.getId(), s.getTitle(), s.getCreatedAt(), s.getUpdatedAt());
    }
}