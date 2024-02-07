package com.olim.customerservice.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CustomerRole {
    CUSTOMER_USER("CUSTOMER_USER"),
    CUSTOMER_INSTRUCTOR("CUSTOMER_INSTRUCTOR"),
    CUSTOMER_ADMIN("CUSTOMER_ADMIN");
    private final String key;
}
