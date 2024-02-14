package com.olim.customerservice.dto.response;

import com.olim.customerservice.entity.Instructor;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public record InstructorGetListByCenterResponse(
        Long total,
        List<InstructorGetResponse> hits
) {
    public static InstructorGetListByCenterResponse makeDto(Page<Instructor> instructors) {
        List<InstructorGetResponse> responses = new ArrayList<>();
        for (Instructor i : instructors.getContent()) {
            InstructorGetResponse instructorGetResponse =
                    InstructorGetResponse.makeDto(i);
            responses.add(instructorGetResponse);
        }
        InstructorGetListByCenterResponse response =
                new InstructorGetListByCenterResponse(instructors.getTotalElements(), responses);
        return response;
    }
}
