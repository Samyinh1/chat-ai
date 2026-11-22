package com.chatapp.backend.session;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chatapp.backend.session.dto.CreateSessionRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class SessionService {

    private final ChatSessionRepository sessions;
    private final ChatMessageRepository messages;

    public ChatSession create(UUID ownerId, CreateSessionRequest req) {
        ChatSession s = new ChatSession(ownerId);
        if (req != null) {
            if (req.title() != null && !req.title().isBlank()) {
                s.setTitle(req.title().trim());
            }
            s.setSystemPrompt(req.systemPrompt());
        }
        return sessions.save(s);
    }

    @Transactional(readOnly = true)
    public List<ChatSession> list(UUID ownerId) {
        return sessions.findByOwnerIdOrderByUpdatedAtDesc(ownerId);
    }

    public ChatSession rename(UUID ownerId, UUID id, String title) {
        ChatSession s = getOwned(ownerId, id);
        s.setTitle(title.trim());
        s.setUpdatedAt(Instant.now());
        return s; // flushed on commit via dirty checking
    }

    public void delete(UUID ownerId, UUID id) {
        sessions.delete(getOwned(ownerId, id)); // messages removed by ON DELETE CASCADE
    }

    @Transactional(readOnly = true)
    public List<ChatMessage> messages(UUID ownerId, UUID id) {
        getOwned(ownerId, id); // ownership check first
        return messages.findBySessionIdOrderByCreatedAtAscIdAsc(id);
    }

    public ChatSession getOwned(UUID ownerId, UUID id) {
        return sessions.findByIdAndOwnerId(id, ownerId)
                .orElseThrow(() -> new SessionNotFoundException(id));
    }
}