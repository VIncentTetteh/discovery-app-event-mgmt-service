package com.discovery.eventservice.mapper;

import com.discovery.eventservice.dto.request.PaymentRequest;
import com.discovery.eventservice.dto.response.PaymentResponse;
import com.discovery.eventservice.model.Payment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    Payment toEntity(PaymentRequest request);

    PaymentResponse toResponse(Payment payment);
}

