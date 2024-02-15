package com.olim.customerservice.dto.response;

import com.olim.customerservice.entity.Center;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record CenterGetResponse(
        @Schema(description = "센터 UUID", example = "asdf-qwef-vasd")
        UUID id,
        @Schema(description = "센터 이름", example = "1센터")
        String name,
        @Schema(description = "센터 설명", example = "1센터 설명")
        String description,
        @Schema(description = "센터 전화번호", example = "010-1234-5678")
        String phoneNumber,
        @Schema(description = "센터 이메일", example = "pyre@pyre.com")
        String email,
        @Schema(description = "센터 주소", example = "서울시 강남구")
        String address,
        @Schema(description = "센터 상세주소", example = "강남1로 19-202")
        String detailAddress,
        @Schema(description = "센터 이미지 URL", example = "https://www.naver.com")
        String imageUrl
) {
    public static CenterGetResponse makeDto(Center center) {
        CenterGetResponse response = new CenterGetResponse(
                center.getId(),
                center.getName(),
                center.getDescription(),
                center.getPhoneNumber(),
                center.getEmail(),
                center.getAddress(),
                center.getDetailAddress(),
                center.getImageUrl()
        );
        return response;
    }
}
