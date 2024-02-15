package com.olim.customerservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record CenterCreateRequest(
        @NotBlank(message = "헬스장 이름은 필수입니다.")
        @Schema(description = "센터 이름", example = "1센터")
        String name
) {
}
