package com.olim.customerservice.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum InstructorStatus {
    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE"),
    CENTER_DELETED("CENTER_DELETED"),
    DELETE("DELETE");
    private final String key;
}
