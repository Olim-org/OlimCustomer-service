package com.olim.customerservice.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Gender {
    MALE("MALE"),
    FEMALE("FEMALE");
    private final String key;
}
