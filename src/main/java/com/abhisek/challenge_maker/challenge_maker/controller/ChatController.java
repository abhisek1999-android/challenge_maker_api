package com.abhisek.challenge_maker.challenge_maker.controller;

import com.abhisek.challenge_maker.challenge_maker.model.ChatMessage;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

//    private final SimpMessagingTemplate messagingTemplate;
//
//    public ChatController(SimpMessagingTemplate messagingTemplate) {
//        this.messagingTemplate = messagingTemplate;
//    }
//
//    @MessageMapping("/chat/{recipientId}") // listens to /app/chat/{recipientId}
//    public void sendEncryptedMessage(@DestinationVariable Long recipientId,
//                                     ChatMessage message) {
//        System.out.println("Sending message to recipient: " + recipientId+", message: " + message);
//        messagingTemplate.convertAndSendToUser(
//                recipientId.toString(), "/queue/messages", message);
//    }
//
//
//    @MessageMapping("/sendMessage")
//    @SendTo("/topic/public")
//    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
//
//        return chatMessage;
//
//    }
//
//    @MessageMapping("/addUser")
//    @SendTo("/topic/public")
//    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
//
//        // Add username in web socket session
//        headerAccessor.getSessionAttributes().put("username", chatMessage.getSenderId());
//        return chatMessage;
//
//    }.

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(
            @Payload ChatMessage chatMessage
    ) {
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(
            @Payload ChatMessage chatMessage,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSenderId());
        return chatMessage;
    }
}

