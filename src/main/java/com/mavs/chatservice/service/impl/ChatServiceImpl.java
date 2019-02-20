package com.mavs.chatservice.service.impl;

import com.google.common.collect.Lists;
import com.mavs.chatservice.client.UserClient;
import com.mavs.chatservice.dto.ChatDto;
import com.mavs.chatservice.dto.ChatMessageDto;
import com.mavs.chatservice.model.Chat;
import com.mavs.chatservice.model.ChatMessage;
import com.mavs.chatservice.model.Participant;
import com.mavs.chatservice.repository.ChatRepository;
import com.mavs.chatservice.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final UserClient userClient;

    public ChatServiceImpl(ChatRepository chatRepository, UserClient userClient) {
        this.chatRepository = chatRepository;
        this.userClient = userClient;
    }

    @Override
    public List<Chat> getAll() {
        log.info("Getting all existing chats!");
        return chatRepository.findAll();
    }

    @Override
    public Optional<Chat> getById(String id) {
        log.info("Getting Chat by id: {}", id);
        return chatRepository.findById(id);
    }

    @Override
    public Optional<Chat> getByName(String name) {
        log.info("Getting Chat by name: {}", name);
        return chatRepository.getByName(name);
    }

    @Override
    public Chat save(ChatDto chatDto) {
        return getByName(chatDto.getName()).orElseGet(() -> chatRepository.save(transformDtoToObject(chatDto)));
    }

    @Async
    @Override
    public void saveMessageToChat(ChatMessageDto chatMessageDto, String topicName) {
        log.info("current thread name in async: {}", Thread.currentThread().getId());
        Optional<Chat> chatOptional = chatRepository.getByName(topicName);
        chatOptional.ifPresent(chat -> {
            List<ChatMessage> chatMessages = chat.getChatMessages();
            if (chatMessages == null) {
                chatMessages = Lists.newArrayList();
            }
            chatMessages.add(new ChatMessage(chatMessageDto));
            chat.setChatMessages(chatMessages);
            chatRepository.save(chat);
        });
    }

    @Override
    public Chat update(ChatDto chatDto) {
        Optional<Chat> chatOptional = getByName(chatDto.getName());
        if (chatOptional.isPresent()) {

        } else {

        }

        return chatRepository.save(transformDtoToObject(chatDto));
    }

    @Override
    public void delete(String chatId) {
        chatRepository.deleteById(chatId);
    }

    @Async
    @Override
    public void addParticipantToChat(ChatMessageDto chatMessageDto, String topicName) {
        log.info("current thread name in async: {}", Thread.currentThread().getId());
        Optional<Chat> chatOptional = chatRepository.getByName(topicName);
        Chat chat = chatOptional.orElseGet(() -> chatRepository.save(Chat.builder()
                .name(topicName)
                .participants(Lists.newArrayList(new Participant("", chatMessageDto.getSender())))
                .build()));

        if (chat.getParticipants().stream().noneMatch(participant ->
                chatMessageDto.getSender().equalsIgnoreCase(participant.getName()))) {
            chat.getParticipants().add(new Participant(Strings.EMPTY, chatMessageDto.getSender()));
            chatRepository.save(chat);
        }
    }

    @Override
    public void deleteByName(String chatName) {
        chatRepository.getByName(chatName).ifPresent(chatRepository::delete);
    }

    private Chat transformDtoToObject(ChatDto chatDto) {
        return Chat.builder()
                .name(chatDto.getName())
                .participants(chatDto.getParticipants().stream()
//                        .map(userClient::findUserByEmail)
//                        .filter(Optional::isPresent)
//                        .map(activityUserDto -> new Participant(activityUserDto.get().getEmail(), activityUserDto.get().getUsername()))
                        .map(name -> new Participant(Strings.EMPTY, name))
                        .collect(Collectors.toList()))
                .build();

    }
}
