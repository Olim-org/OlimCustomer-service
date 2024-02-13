package com.olim.customerservice.dto.request;

import com.olim.customerservice.enumeration.VisitRoute;

import java.util.List;

public record CustomerPutProfileRequest(
    List<VisitRoute> visitRoute,
    String healthExp,
    String purpose,
    String diseases,
    String pregnant,
    String lifeHabit,
    String desiredTimeSlot,
    Boolean kakaoTalkAlert
) {
}
