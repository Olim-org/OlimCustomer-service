package com.olim.customerservice.dto.request;

import java.util.List;

public record RouteAndIdRequest(
        String route,
        List<Long> ids
) {
    public static RouteAndIdRequest makeDto(String route, List<Long> ids) {
        return new RouteAndIdRequest(route, ids);
    }
}
