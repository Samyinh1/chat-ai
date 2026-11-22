package com.chatapp.backend.session.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RenameSessionRequest(
        @NotBlank @Size(max = Limits.MAX_TITLE_CHARS) String title) {
}