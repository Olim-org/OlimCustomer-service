package com.olim.customerservice.dto.response;

import com.olim.customerservice.entity.Center;

import java.util.ArrayList;
import java.util.List;

public record CenterGetListResponse(
        int total,
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
