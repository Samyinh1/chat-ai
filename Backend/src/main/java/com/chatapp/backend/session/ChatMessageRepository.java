package com.chatapp.backend.session;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, UUID> {

    // Order by created_at, tie-broken by id; never trust client order.
    List<ChatMessage> findBySessionIdOrderByCreatedAtAscIdAsc(UUID sessionId);
}
