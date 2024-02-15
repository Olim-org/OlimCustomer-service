package com.olim.customerservice.dto.response;

import com.olim.customerservice.entity.Center;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record CenterFeignResponse(
        @Schema(description = "센터 UUID", example = "asdfw-rwqvc-vx")
        UUID centerId,
        @Schema(description = "센터 소유자 UUID", example = "asdfw-rwqvc-vx")
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
