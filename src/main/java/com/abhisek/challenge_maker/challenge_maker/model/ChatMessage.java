package com.abhisek.challenge_maker.challenge_maker.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessage {

    private String senderId;
    private String receiverId;
    private String encryptedMessage;
    private String timestamp;
    private MessageType type;
}
