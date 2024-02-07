package com.olim.customerservice.dto.response;

import com.olim.customerservice.entity.Instructor;

import java.util.ArrayList;
import java.util.List;

public record InstructorGetListByCenterResponse(
        int total,
        List<InstructorGetResponse> hits
) {
    public static InstructorGetListByCenterResponse makeDto(List<Instructor> instructors) {
        List<InstructorGetResponse> responses = new ArrayList<>();
        for (Instructor i : instructors) {
            InstructorGetResponse instructorGetResponse =
                    InstructorGetResponse.makeDto(i);
            responses.add(instructorGetResponse);
        }
        InstructorGetListByCenterResponse response =
                new InstructorGetListByCenterResponse(responses.size(), responses);
        return response;
    }
}
