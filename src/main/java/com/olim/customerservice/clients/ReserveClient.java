package com.olim.customerservice.clients;

import com.olim.customerservice.dto.response.CenterNewCustomerResponse;
import com.olim.customerservice.dto.response.RouteSalseResponse;
import com.olim.customerservice.dto.response.TicketSalesResponse;
import com.olim.customerservice.enumeration.VisitRoute;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(value = "reserve-service", path = "/reserve-service")
public interface ReserveClient {
    @GetMapping("/ticket-customer/isValid")
    CenterNewCustomerResponse getTicketCustomersIsValid(
            @RequestHeader("id") String userId,
            @RequestParam(value = "centerId") String centerId,
            @RequestParam(value = "customerIds") List<Long> customerIds);
    @GetMapping("/ticket-customer/sales")
    List<TicketSalesResponse> getTicketSales(
            @RequestHeader("id") String userId,
            @RequestParam(value = "centerId") String centerId,
            @RequestParam(value = "startDate") String startDate,
            @RequestParam(value = "endDate") String endDate);
    @GetMapping("/ticket-customer/routeSales")
    List<RouteSalseResponse> getRouteTicketSales(
            @RequestHeader("id") String userId,
            @RequestParam(value = "centerId") String centerId,
            @RequestParam(value = "routeAndId") Map<String, List<Long>> routeAndId);

}
