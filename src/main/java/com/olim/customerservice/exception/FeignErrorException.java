package com.olim.customerservice.exception;

public record FeignErrorException(
        int status,
        String divisionCode,
        String resultMsg,
        String errors,
        String reason

) {
}
