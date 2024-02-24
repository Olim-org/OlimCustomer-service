package com.olim.customerservice.dto.response;



import java.util.List;

public record RouteSalseResponse(
        String routeName,
        List<RouteTicketSalesResponse> value
) {

}
