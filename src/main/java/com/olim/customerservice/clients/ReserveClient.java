package com.olim.customerservice.clients;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "reserve-service", path = "/reserve-service")
public interface ReserveClient {
    
}
