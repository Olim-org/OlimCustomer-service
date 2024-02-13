package com.olim.customerservice.dto.response;

import com.olim.customerservice.entity.Center;

import java.util.UUID;

public record CenterFeignResponse(
        UUID centerId,
        UUID owner
) {
    public static CenterFeignResponse makeDto(Center center) {
        CenterFeignResponse centerFeignResponse = new CenterFeignResponse(
                center.getId(),
                center.getOwner()
        );
        return centerFeignResponse;
    }
}
