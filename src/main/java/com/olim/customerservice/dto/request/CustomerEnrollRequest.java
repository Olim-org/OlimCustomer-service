package com.olim.customerservice.dto.request;

import com.olim.customerservice.enumeration.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

public record CustomerEnrollRequest(
        @NotNull
        @Schema(description = "고객 이름", example = "김영웅")
        @Pattern(regexp = "^[가-힣]{2,10}$", message = "이름은 한글 2~10자로 입력해주세요.")
        String name,
        @NotBlank
        @Schema(description = "고객 성별", example = "FEMALE")
        Gender gender,
        @NotBlank
        @Schema(description = "고객 번호", example = "01099998888")
        @Pattern(regexp = "^\\d{3}\\d{3,4}\\d{4}$", message = "전화번호 형식이 올바르지 않습니다.")
        String phoneNumber,
        @NotBlank
        @Schema(description = "고객 생년월일", example = "2002-01-01")
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "생년월일 형식이 올바르지 않습니다.")
        String birthDate,
        @NotBlank
        @Schema(description = "고객 주소", example = "울산광역시 울주군 ..")
        String address,
        @Schema(description = "강사 ID", example = "5")
        Long instructorId,
        @Schema(description = "센터 UUID", example = "asdfw-rwqvc-vx")
        UUID centerId
) {
}
