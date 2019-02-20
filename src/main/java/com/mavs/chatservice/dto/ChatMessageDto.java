package com.mavs.chatservice.dto;

import lombok.Data;


@Data
public class ChatMessageDto {

    private MessageType type;
    private String content;
    private String sender;
}
