package com.olim.customerservice.dto.request;

import com.olim.customerservice.enumeration.CustomerStatus;
import com.olim.customerservice.enumeration.Gender;
import com.olim.customerservice.enumeration.VisitRoute;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.List;
import java.util.UUID;

public record CustomerPutProfileRequest(
    @NotBlank
    @Pattern(regexp = "^[가-힣]{2,10}$", message = "이름은 한글 2~10자로 입력해주세요.")
    @Schema(description = "고객 이름", example = "김영웅")
    String name,
    @NotBlank
    @Schema(description = "고객 성별", example = "FEMALE")
    Gender gender,
    @NotBlank
    @Pattern(regexp = "^\\d{3}\\d{3,4}\\d{4}$", message = "전화번호 형식이 올바르지 않습니다.")
    @Schema(description = "고객 번호", example = "01099998888")
    String phoneNumber,
    @NotBlank
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "생년월일 형식이 올바르지 않습니다.")
    @Schema(description = "고객 생년월일", example = "2002-01-01")
    String birthDate,
    @NotBlank
    @Schema(description = "고객 주소", example = "울산광역시 울주군 ..")
    String address,
    @Schema(description = "강사 ID", example = "5")
    Long instructorId,
    @Schema(description = "센터 UUID", example = "asdfw-rwqvc-vx")
    UUID centerId,
    @Schema(description = "방문 경로", example = "[INSTAGRAM]")
    List<VisitRoute> visitRoute,
    @Schema(description = "고객 건강 상태", example = "HEALTHY")
    String healthExp,
    @Schema(description = "고객 목적", example = "WEIGHT_LOSS")
    String purpose,
    @Schema(description = "고객 질병", example = "NONE")
    String diseases,
    @Schema(description = "고객 임신 여부", example = "NONE")
    String pregnant,
    @Schema(description = "고객 생활 습관", example = "NONE")
    String lifeHabit,
    @Schema(description = "원하는 운동 시간대", example = "NONE")
    String desiredTimeSlot,
    @Schema(description = "카카오톡 알림 여부", example = "true")
    Boolean kakaoTalkAlert,
    @Schema(description = "고객 상태", example = "ACTIVE OR INACTIVE")
    CustomerStatus status
) {
}
