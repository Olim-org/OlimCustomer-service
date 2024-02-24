package com.olim.customerservice.dto.response;

public record CenterVisitRouteResponse(
    String name,
    String value
) {
    public static CenterVisitRouteResponse makeDto(String name, String value) {
        return new CenterVisitRouteResponse(name, value);
    }
}
