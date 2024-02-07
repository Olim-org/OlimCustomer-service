package com.olim.customerservice.dto.response;

import com.olim.customerservice.entity.Instructor;

public record InstructorGetResponse(
        Long id,
        String name
) {
    public static InstructorGetResponse makeDto(Instructor instructor) {
        InstructorGetResponse response = new InstructorGetResponse(
                instructor.getId(),
                instructor.getName()
        );
        return response;
    }
}
