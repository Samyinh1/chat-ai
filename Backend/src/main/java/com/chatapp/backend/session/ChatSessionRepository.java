package com.chatapp.backend.session;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatSessionRepository extends JpaRepository<ChatSession, UUID> {

    List<ChatSession> findByOwnerIdOrderByUpdatedAtDesc(UUID ownerId);

    // BR-6: every lookup is scoped by owner; a foreign id yields empty (-> 404).
    Optional<ChatSession> findByIdAndOwnerId(UUID id, UUID ownerId);
}
