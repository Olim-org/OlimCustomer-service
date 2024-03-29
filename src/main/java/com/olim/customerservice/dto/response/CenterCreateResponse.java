package com.olim.customerservice.dto.response;

import com.olim.customerservice.dto.request.CenterCreateRequest;
import com.olim.customerservice.entity.Center;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record CenterCreateResponse(
        @Schema(description = "센터 UUID", example = "asdf-qwef-vasd")
        UUID id,
        @Schema(description = "센터 이름", example = "1센터")
        String name
) {
    public static CenterCreateResponse makeDto(Center center) {
        CenterCreateResponse response = new CenterCreateResponse(
                center.getId(), center.getName()
        );
        return response;
    }
}
