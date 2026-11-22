package com.chatapp.backend.session.dto;

import jakarta.validation.constraints.Size;

public record CreateSessionRequest(
        @Size(max = Limits.MAX_TITLE_CHARS) String title,
        @Size(max = Limits.MAX_MESSAGE_CHARS) String systemPrompt) {
}