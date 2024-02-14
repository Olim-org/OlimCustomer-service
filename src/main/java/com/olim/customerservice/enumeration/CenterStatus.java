package com.olim.customerservice.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CenterStatus {
    WAIT("WAIT"),
    OPEN("OPEN"),
    DELETE("DELETE");
    private final String key;
}
