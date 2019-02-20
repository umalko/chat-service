package com.mavs.chatservice.controller;

import com.mavs.chatservice.dto.ChatDto;
import com.mavs.chatservice.model.Chat;
import com.mavs.chatservice.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1/chat")
public class CrudChatController {

    private final ChatService chatService;

    public CrudChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/id/{id}")
    public Optional<Chat> getById(@PathVariable("id") String id) {
        return chatService.getById(id);
    }

    @GetMapping("/name/{name}")
    public Optional<Chat> getByName(@PathVariable("name") String name) {
        return chatService.getByName(name);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void create(@RequestBody ChatDto chatDto) {
        log.info("========= {}", chatDto);
        chatService.save(chatDto);
    }

    @DeleteMapping("/name/{chatName}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByName(@PathVariable("chatName") String chatName) {
        chatService.deleteByName(chatName);
    }
}
