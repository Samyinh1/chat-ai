package com.chatapp.backend.session;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.chatapp.backend.identity.UserIdentityFilter;
import com.chatapp.backend.session.dto.CreateSessionRequest;
import com.chatapp.backend.session.dto.MessageResponse;
import com.chatapp.backend.session.dto.RenameSessionRequest;
import com.chatapp.backend.session.dto.SessionResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService service;

    @GetMapping
    public List<SessionResponse> list(@RequestAttribute(UserIdentityFilter.ATTR) UUID owner) {
        return service.list(owner).stream().map(SessionResponse::from).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SessionResponse create(@RequestAttribute(UserIdentityFilter.ATTR) UUID owner,
                                  @Valid @RequestBody(required = false) CreateSessionRequest req) {
        return SessionResponse.from(service.create(owner, req));
    }

    @PatchMapping("/{id}")
    public SessionResponse rename(@RequestAttribute(UserIdentityFilter.ATTR) UUID owner,
                                  @PathVariable UUID id,
                                  @Valid @RequestBody RenameSessionRequest req) {
        return SessionResponse.from(service.rename(owner, id, req.title()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestAttribute(UserIdentityFilter.ATTR) UUID owner, @PathVariable UUID id) {
        service.delete(owner, id);
    }

    @GetMapping("/{id}/messages")
    public List<MessageResponse> messages(@RequestAttribute(UserIdentityFilter.ATTR) UUID owner,
                                          @PathVariable UUID id) {
        return service.messages(owner, id).stream().map(MessageResponse::from).toList();
    }
}