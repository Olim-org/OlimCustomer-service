package com.olim.customerservice.dto.request;

import java.util.UUID;

public record CustomerAttendFeignRequest(
        Long customerId,
        UUID attendId,
        Boolean isBlackConsumer
) {
}
