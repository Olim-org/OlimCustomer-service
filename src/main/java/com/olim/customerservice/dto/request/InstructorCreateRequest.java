package com.olim.customerservice.dto.request;

import java.util.UUID;

public record InstructorCreateRequest(
        UUID centerId,
        Long userId
) {
}
