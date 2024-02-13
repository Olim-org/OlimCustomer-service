package com.olim.customerservice.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VisitRoute {
    INSTAGRAM("INSTAGRAM"),
    BLOG("BLOG"),
    OTHERS("OTHERS");
    private final String key;
}
