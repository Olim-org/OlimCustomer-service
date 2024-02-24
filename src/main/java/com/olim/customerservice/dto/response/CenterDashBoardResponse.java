package com.olim.customerservice.dto.response;

import com.olim.customerservice.entity.Center;

import java.util.List;

public record CenterDashBoardResponse(
    CenterNewCustomerResponse newCustomers,
    List<TicketSalesResponse> ticketSales,
    List<RouteSalseResponse> routeSales,
    List<CenterVisitRouteResponse> visitRoutes
) {
    public static CenterDashBoardResponse makeDto(
            CenterNewCustomerResponse newCustomers,
            List<TicketSalesResponse> sales,
            List<RouteSalseResponse> routeSales,
            List<CenterVisitRouteResponse> visitRoutes
    ) {
        CenterDashBoardResponse response = new CenterDashBoardResponse(
                newCustomers, sales, routeSales, visitRoutes
        );
        return response;
    }
}
