package com.olim.customerservice.dto.request;

public record CenterModifyRequest(
        String name,
        String description,
        String phoneNumber,
        String address,
        String imageUrl
) {
}
