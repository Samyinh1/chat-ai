package com.chatapp.backend;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.chatapp.backend.session.ChatMessage;
import com.chatapp.backend.session.ChatMessageRepository;
import com.jayway.jsonpath.JsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
class SessionApiTests {

    static final String H = "X-Owner-Id";

    @Autowired MockMvc mvc;
    @Autowired ChatMessageRepository messageRepo;

    // Random owners per test keep tests isolated on the shared container.
    final String alice = UUID.randomUUID().toString();
    final String bob = UUID.randomUUID().toString();

    String createSession(String owner, String title) throws Exception {
        String body = mvc.perform(post("/api/sessions").header(H, owner)
                        .contentType(APPLICATION_JSON)
                        .content("{\"title\":\"" + title + "\"}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return JsonPath.read(body, "$.id");
    }

    @Test
    void missingHeaderIs400() throws Exception {
        mvc.perform(get("/api/sessions")).andExpect(status().isBadRequest());
    }

    @Test
    void invalidHeaderIs400() throws Exception {
        mvc.perform(get("/api/sessions").header(H, "not-a-uuid")).andExpect(status().isBadRequest());
    }

    @Test
    void listIsNewestFirstAndOwnerScoped() throws Exception {
        createSession(alice, "first");
        Thread.sleep(5);
        createSession(alice, "second");
        createSession(bob, "bobs");

        mvc.perform(get("/api/sessions").header(H, alice))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("second"))
                .andExpect(jsonPath("$[1].title").value("first"));
    }

    @Test
    void renameWorksAndRejectsBlankTitle() throws Exception {
        String id = createSession(alice, "old");

        mvc.perform(patch("/api/sessions/" + id).header(H, alice)
                        .contentType(APPLICATION_JSON).content("{\"title\":\"new\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("new"));

        mvc.perform(patch("/api/sessions/" + id).header(H, alice)
                        .contentType(APPLICATION_JSON).content("{\"title\":\"  \"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void anotherOwnersSessionReturns404() throws Exception {
        String id = createSession(alice, "private");

        mvc.perform(get("/api/sessions/" + id + "/messages").header(H, bob))
                .andExpect(status().isNotFound());
        mvc.perform(patch("/api/sessions/" + id).header(H, bob)
                        .contentType(APPLICATION_JSON).content("{\"title\":\"x\"}"))
                .andExpect(status().isNotFound());
        mvc.perform(delete("/api/sessions/" + id).header(H, bob))
                .andExpect(status().isNotFound());

        // The real owner is unaffected.
        mvc.perform(get("/api/sessions/" + id + "/messages").header(H, alice))
                .andExpect(status().isOk());
    }

    @Test
    void messagesAreReturnedInOrder() throws Exception {
        String id = createSession(alice, "chat");
        UUID sid = UUID.fromString(id);
        messageRepo.save(new ChatMessage(sid, ChatMessage.Role.user, "hi"));
        messageRepo.save(new ChatMessage(sid, ChatMessage.Role.assistant, "hello"));

        mvc.perform(get("/api/sessions/" + id + "/messages").header(H, alice))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].role").value("user"))
                .andExpect(jsonPath("$[1].content").value("hello"));
    }

    @Test
    void deleteReturns204ThenSessionIsGone() throws Exception {
        String id = createSession(alice, "tmp");

        mvc.perform(delete("/api/sessions/" + id).header(H, alice))
                .andExpect(status().isNoContent());
        mvc.perform(get("/api/sessions/" + id + "/messages").header(H, alice))
                .andExpect(status().isNotFound());
    }

    @Test
    void oversizedSystemPromptIs400() throws Exception {
        mvc.perform(post("/api/sessions").header(H, alice).contentType(APPLICATION_JSON)
                        .content("{\"systemPrompt\":\"" + "a".repeat(8001) + "\"}"))
                .andExpect(status().isBadRequest());
    }
}