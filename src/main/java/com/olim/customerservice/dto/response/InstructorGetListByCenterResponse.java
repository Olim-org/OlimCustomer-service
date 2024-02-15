package com.olim.customerservice.dto.response;

import com.olim.customerservice.entity.Instructor;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public record InstructorGetListByCenterResponse(
        @Schema(description = "총 강사 수", example = "10")
        Long total,
        @Schema(description = "강사 목록")
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
