package com.olim.customerservice.dto.response;

import com.olim.customerservice.entity.Instructor;
import com.olim.customerservice.enumeration.Gender;

import java.time.format.DateTimeFormatter;

public record InstructorGetResponse(
        Long uid,
        String centerName,
        Long id,
        String name,
        Gender gender,
        String birthDate,
        String phoneNumber,
        String address
) {
    public static InstructorGetResponse makeDto(Instructor instructor) {
        InstructorGetResponse response = new InstructorGetResponse(
                instructor.getId(),
                instructor.getCenter().getName(),
                instructor.getCenterInstructorId(),
                instructor.getName(),
                instructor.getGender(),
                instructor.getBirthDate().format(DateTimeFormatter.ISO_DATE),
                instructor.getPhoneNumber(),
                instructor.getAddress()
        );
        return response;
    }
}
