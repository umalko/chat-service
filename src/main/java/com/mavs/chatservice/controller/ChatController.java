package com.mavs.chatservice.controller;

import com.mavs.chatservice.dto.ChatMessageDto;
import com.mavs.chatservice.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @MessageMapping("/{topicName}/sendMessage")
    @SendTo("/topic/public")
    public ChatMessageDto sendMessage(@Payload ChatMessageDto chatMessageDto, @DestinationVariable String topicName) {
        log.info("---- {}", chatMessageDto);
        log.info("current thread name in controller: {}", Thread.currentThread().getId());
        chatService.saveMessageToChat(chatMessageDto, topicName);
        return chatMessageDto;
    }

    @MessageMapping("/{topicName}/addUser")
    @SendTo("/topic/public")
    public ChatMessageDto addUser(@Payload ChatMessageDto chatMessageDto,
                                  SimpMessageHeaderAccessor headerAccessor, @DestinationVariable String topicName) {
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", chatMessageDto.getSender());
        chatService.addParticipantToChat(chatMessageDto, topicName);
        return chatMessageDto;
    }

}