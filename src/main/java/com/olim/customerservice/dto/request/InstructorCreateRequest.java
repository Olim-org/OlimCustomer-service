package com.olim.customerservice.dto.request;

import com.olim.customerservice.enumeration.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

public record InstructorCreateRequest(
        @NotBlank
        @Pattern(regexp = "^[가-힣]{2,10}$", message = "이름은 한글 2~10자로 입력해주세요.")
        @Schema(description = "강사 이름", example = "김영웅")
        String name,
        @NotNull
        @Schema(description = "강사 성별", example = "FEMALE")
        Gender gender,
        @NotBlank
        @Pattern(regexp = "^\\d{3}\\d{3,4}\\d{4}$", message = "전화번호 형식이 올바르지 않습니다.")
        @Schema(description = "강사 번호", example = "01099998888")
        String phoneNumber,
        @NotBlank
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "생년월일 형식이 올바르지 않습니다.")
        @Schema(description = "강사 생년월일", example = "2002-01-01")
        String birthDate,
        @NotBlank
        @Schema(description = "강사 주소", example = "울산광역시 울주군 ..")
        String address,
        @Schema(description = "센터 UUID", example = "asdfw-rwqvc-vx")
        UUID centerId

) {
}
