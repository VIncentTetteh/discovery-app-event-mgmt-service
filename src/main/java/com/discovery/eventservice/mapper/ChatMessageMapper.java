package com.discovery.eventservice.mapper;

import com.discovery.eventservice.dto.request.ChatMessageRequest;
import com.discovery.eventservice.dto.request.ChatMessageResponse;
import com.discovery.eventservice.model.ChatMessage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChatMessageMapper {

    ChatMessage toEntity(ChatMessageRequest request);

    ChatMessageResponse toResponse(ChatMessage chatMessage);
}

