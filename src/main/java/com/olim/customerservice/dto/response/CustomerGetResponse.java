package com.olim.customerservice.dto.response;

import com.olim.customerservice.entity.Center;
import com.olim.customerservice.entity.Customer;
import com.olim.customerservice.enumeration.Gender;
import com.olim.customerservice.enumeration.VisitRoute;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.swing.text.DateFormatter;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

public record CustomerGetResponse(
        @Schema(description = "고객 UUID", example = "asdfw-rwqvc-vx")
        Long uid,
        @Schema(description = "센터 이름", example = "울산광역시 울주군 센터")
        String centerName,
        @Schema(description = "센터 고객 ID", example = "1234")
        Long id,
        @Schema(description = "고객 이름", example = "김영웅")
        String name,
        @Schema(description = "고객 성별", example = "FEMALE")
        Gender gender,
        @Schema(description = "고객 생년월일", example = "2002-01-01")
        String birthDate,
        @Schema(description = "고객 번호", example = "01099998888")
        String phoneNumber,
        @Schema(description = "고객 주소", example = "울산광역시 울주군 ..")
        String address,
        @Schema(description = "강사 ID", example = "5")
        Long instructorId,
        @Schema(description = "강사 이름", example = "김영웅")
        String instructorName,
        @Schema(description = "방문 경로", example = "INSTAGRAM")
        VisitRoute visitRoute,
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
        @Schema(description = "고객 생성 일", example = "YYYY-MM-dd HH:mm")
        String cAt,
        @Schema(description = "기타 사유", example = "NONE")
        String othersReason

) {
    public static CustomerGetResponse makeDto(Customer customer) {
        CustomerGetResponse response = new CustomerGetResponse(
                customer.getId(),
                customer.getCenter().getName(),
                customer.getCenterCustomerId(),
                customer.getName(),
                customer.getGender(),
                customer.getBirthDate().format(DateTimeFormatter.ISO_DATE),
                customer.getPhoneNumber(),
                customer.getAddress(),
                customer.getInstructor() != null ? customer.getInstructor().getId() : null,
                customer.getInstructor() != null ? customer.getInstructor().getName() : null,
                customer.getVisitRoute(),
                customer.getHealthExp(),
                customer.getPurpose(),
                customer.getDiseases(),
                customer.getPregnant(),
                customer.getLifeHabit(),
                customer.getDesiredTimeSlot(),
                customer.getKakaoTalkAlert(),
                customer.getCAt().format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm")),
                customer.getOthersReason()
        );
        return response;
    }
}
