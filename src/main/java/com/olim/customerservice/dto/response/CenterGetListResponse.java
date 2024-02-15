package com.olim.customerservice.dto.response;

import com.olim.customerservice.entity.Center;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

public record CenterGetListResponse(
        @Schema(description = "총 센터 수", example = "10")
        int total,
        @Schema(description = "센터 목록")
        List<CenterGetResponse> hits
) {
    public static CenterGetListResponse makeDto(List<Center> centers) {
        List<CenterGetResponse> centerGetResponses = new ArrayList<>();
        for (Center c : centers) {
            CenterGetResponse centerGetResponse = CenterGetResponse.makeDto(c);
            centerGetResponses.add(centerGetResponse);
        }
        CenterGetListResponse response = new CenterGetListResponse(centerGetResponses.size(), centerGetResponses);
        return response;
    }
}
