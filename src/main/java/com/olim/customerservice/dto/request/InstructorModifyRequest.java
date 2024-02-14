package com.olim.customerservice.dto.request;

import com.olim.customerservice.enumeration.Gender;
import com.olim.customerservice.enumeration.InstructorStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record InstructorModifyRequest(
        @NotBlank
        @Schema(description = "강사 이름", example = "김영웅")
        String name,
        @NotBlank
        @Schema(description = "강사 성별", example = "FEMALE")
        Gender gender,
        @NotBlank
        @Schema(description = "강사 생년월일", example = "2002-01-01")
        String birthDate,
        @NotBlank
        @Schema(description = "강사 주소", example = "울산광역시 울주군 ..")
        String address,
        @NotBlank
        @Schema(description = "강사 상태", example = "ACTIVE OR INACTIVE")
        InstructorStatus status
) {
}
