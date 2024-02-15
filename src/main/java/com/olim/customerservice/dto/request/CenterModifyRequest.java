package com.olim.customerservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CenterModifyRequest(
        @NotBlank(message = "센터 이름은 필수입니다.")
        @Schema(description = "센터 이름", example = "1센터")
        String name,
        @Nullable
        @Schema(description = "센터 설명", example = "1센터 설명")
        String description,
        @Nullable
        @Schema(description = "센터 전화번호", example = "010-1234-5678")
        @Pattern(regexp = "^\\d{3}\\d{3,4}\\d{4}$", message = "전화번호 형식이 올바르지 않습니다.")
        String phoneNumber,
        @Nullable
        @Schema(description = "센터 이메일", example = "pyre@pyre.com")
        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", message = "이메일 형식이 올바르지 않습니다.")
        @Email
        String email,
        @Nullable
        @Schema(description = "센터 주소", example = "서울시 강남구")
        String address,
        @Nullable
        @Schema(description = "센터 상세주소", example = "강남1로 19-202")
        String detailAddress,
        @Nullable
        @Schema(description = "센터 이미지 URL", example = "https://www.naver.com")
        String imageUrl
) {
}
