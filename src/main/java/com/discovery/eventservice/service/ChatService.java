package com.discovery.eventservice.service;

import com.discovery.eventservice.dto.request.ChatMessageRequest;
import com.discovery.eventservice.dto.request.ChatMessageResponse;

import java.util.List;
import java.util.UUID;

public interface ChatService {
    ChatMessageResponse sendMessage(ChatMessageRequest request);
    List<ChatMessageResponse> getMessagesByEvent(UUID eventId);
}

