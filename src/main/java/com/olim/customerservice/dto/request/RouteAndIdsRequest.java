package com.olim.customerservice.dto.request;

import java.util.List;

public record RouteAndIdsRequest(
        List<RouteAndIdRequest> routeAndIdRequests
) {
}
