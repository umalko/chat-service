package com.mavs.chatservice.model;

import com.mavs.chatservice.dto.ChatMessageDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {

    private String content;
    private String sender;
    private Date created;

    public ChatMessage(ChatMessageDto chatMessageDto) {
        this.content = chatMessageDto.getContent();
        this.sender = chatMessageDto.getSender();
        this.created = new Date();
    }
}
