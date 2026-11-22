package com.chatapp.backend.error;

public record ApiError(int status, String error, String message) {
}