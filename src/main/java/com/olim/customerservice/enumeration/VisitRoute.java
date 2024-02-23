package com.olim.customerservice.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VisitRoute {
    SEARCH("SEARCH"),
    INSTAGRAM("INSTAGRAM"),
    BLOG("BLOG"),
    FRIEND("FRIEND"),
    SIGNBOARD("SIGNBOARD"),

    NONE("NONE"),

    OTHERS("OTHERS");
    private final String key;
}
