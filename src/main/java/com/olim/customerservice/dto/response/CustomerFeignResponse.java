package com.olim.customerservice.dto.response;

import com.olim.customerservice.entity.Customer;

import java.util.UUID;

public record CustomerFeignResponse(
    Long id,
    UUID userId,
    String name,
    String phoneNumber,
    UUID owner
) {
    public static CustomerFeignResponse makeDto(Customer customer) {
        return new CustomerFeignResponse(
            customer.getId(),
            customer.getUserId(),
            customer.getName(),
            customer.getPhoneNumber(),
            customer.getOwner()
        );
    }
}
