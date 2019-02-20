package com.mavs.chatservice.service;

import com.mavs.chatservice.dto.ChatDto;
import com.mavs.chatservice.dto.ChatMessageDto;
import com.mavs.chatservice.model.Chat;

import java.util.List;
import java.util.Optional;

public interface ChatService {

    List<Chat> getAll();

    Optional<Chat> getById(String id);

    Optional<Chat> getByName(String name);

    Chat save(ChatDto chatDto);

    void saveMessageToChat(ChatMessageDto chatMessageDto, String topicName);

    Chat update(ChatDto chatDto);

    void delete(String chatId);

    void addParticipantToChat(ChatMessageDto chatMessageDto, String topicName);

    void deleteByName(String chatName);
}
