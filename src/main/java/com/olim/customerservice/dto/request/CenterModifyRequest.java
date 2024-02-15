package com.olim.customerservice.dto.request;

public record CenterModifyRequest(
        String name,
        String description,
        String phoneNumber,
        String email,
        String address,
        String detailAddress,
        String imageUrl
) {
}
