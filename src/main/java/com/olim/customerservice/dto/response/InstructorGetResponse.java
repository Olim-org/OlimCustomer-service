package com.olim.customerservice.dto.response;

import com.olim.customerservice.entity.Instructor;
import com.olim.customerservice.enumeration.Gender;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.format.DateTimeFormatter;

public record InstructorGetResponse(
        @Schema(description = "강사 UID", example = "5")
        Long uid,
        @Schema(description = "센터 이름", example = "서울센터")
        String centerName,
        @Schema(description = "센터 강사 ID", example = "5")
        Long id,
        @Schema(description = "강사 이름", example = "홍길동")
        String name,
        @Schema(description = "강사 성별", example = "FEMALE")
        Gender gender,
        @Schema(description = "강사 생년월일", example = "2002-01-01")
        String birthDate,
        @Schema(description = "강사 번호", example = "01099998888")
        String phoneNumber,
        @Schema(description = "강사 주소", example = "울산광역시 울주군 ..")
        String address,
        @Schema(description = "강사 생성 일", example = "YYYY-MM-dd HH:mm")
        String cAt
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
                instructor.getAddress(),
                instructor.getCAt().format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm"))
        );
        return response;
    }
}
