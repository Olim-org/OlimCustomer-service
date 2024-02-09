package com.olim.customerservice.controller;

import com.olim.customerservice.dto.request.CenterCreateRequest;
import com.olim.customerservice.dto.response.CenterCreateResponse;
import com.olim.customerservice.dto.response.CenterGetListResponse;
import com.olim.customerservice.dto.response.CenterGetResponse;
import com.olim.customerservice.service.CenterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/customer-service/center")
@Tag(name = "Center", description = "Center API 구성")
@Validated
public class CenterController {
    private final CenterService centerService;
    @PostMapping("/create")
    @Operation(description = "센터 생성하기")
    @Parameters({
            @Parameter(name = "userId", description = "액세스 토큰 아이디", required = true, in = ParameterIn.HEADER),
            @Parameter(name = "token", description = "액세스 토큰", required = true, in = ParameterIn.HEADER)
    })
    public ResponseEntity<CenterCreateResponse> createCenter(
            @RequestBody CenterCreateRequest centerCreateRequest,
            @RequestHeader("id") String userId,
            @RequestHeader("Authorization") String token
    ) {
        return new ResponseEntity<>(this.centerService.createCenter(centerCreateRequest, UUID.fromString(userId), token), HttpStatus.OK);
    }
    @GetMapping("/my")
    @Operation(description = "나의 센터 아이디, 이름 가져오기")
    @Parameters({
            @Parameter(name = "userId", description = "액세스 토큰 아이디", required = true, in = ParameterIn.HEADER)
    })
    public ResponseEntity<CenterGetListResponse> getMyCenterList(
            @RequestHeader("id") String userId) {
        return new ResponseEntity<>(this.centerService.getMyCenterList(UUID.fromString(userId)), HttpStatus.OK);
    }
}
