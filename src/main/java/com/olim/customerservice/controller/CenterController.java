package com.olim.customerservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.olim.customerservice.dto.request.CenterCreateRequest;
import com.olim.customerservice.dto.request.CenterModifyRequest;
import com.olim.customerservice.dto.response.*;
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
    @GetMapping("/{centerId}")
    @Operation(description = "센터 정보 가져오기")
    @Parameters({
            @Parameter(name = "userId", description = "액세스 토큰 아이디", required = true, in = ParameterIn.HEADER),
            @Parameter(name = "centerId", description = "센터 UUID", required = true, in = ParameterIn.PATH)
    })
    public ResponseEntity<CenterGetResponse> getCenter(
            @RequestHeader("id") String userId,
            @PathVariable String centerId
    ) {
        return new ResponseEntity<>(this.centerService.getCenter(UUID.fromString(userId), UUID.fromString(centerId)), HttpStatus.OK);
    }
    @PutMapping("/{centerId}")
    @Operation(description = "센터 수정하기")
    @Parameters({
            @Parameter(name = "userId", description = "액세스 토큰 아이디", required = true, in = ParameterIn.HEADER),
            @Parameter(name = "centerId", description = "센터 UUID", required = true, in = ParameterIn.PATH)
    })
    public ResponseEntity<String> updateCenter(
            @RequestHeader("id") String userId,
            @PathVariable String centerId,
            @RequestBody CenterModifyRequest centerModifyRequest
            ) {
        return new ResponseEntity<>(this.centerService.updateCenter(UUID.fromString(userId), UUID.fromString(centerId), centerModifyRequest), HttpStatus.OK);
    }
    @DeleteMapping("/{centerId}")
    @Operation(description = "센터 삭제하기")
    @Parameters({
            @Parameter(name = "userId", description = "액세스 토큰 아이디", required = true, in = ParameterIn.HEADER),
            @Parameter(name = "centerId", description = "센터 UUID", required = true, in = ParameterIn.PATH)
    })
    public ResponseEntity<String> deleteCenter(
            @RequestHeader("id") String userId,
            @PathVariable String centerId
    ) {
        return new ResponseEntity<>(this.centerService.deleteCenter(UUID.fromString(userId), UUID.fromString(centerId)), HttpStatus.OK);
    }
    @Operation(description = "센터 정보 가져오기 Feign 클라이언트 용")
    @GetMapping("/info")
    public CenterFeignResponse getCenterInfo(@RequestHeader("id") String userId, @RequestParam(value = "centerId") String centerId) {
        return this.centerService.getCenterInfo(UUID.fromString(userId), UUID.fromString(centerId));
    }
    @GetMapping("/dashboard")
    @Operation(description = "센터 대시보드 가져오기")
    @Parameters({
            @Parameter(name = "userId", description = "액세스 토큰 아이디", required = true, in = ParameterIn.HEADER),
            @Parameter(name = "centerId", description = "센터 UUID", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "startDate", description = "시작 날짜", in = ParameterIn.QUERY, example = "2021-01-01"),
            @Parameter(name = "endDate", description = "종료 날짜", in = ParameterIn.QUERY, example = "2021-12-31"),
    })
    public ResponseEntity<CenterDashBoardResponse> getCenterDashboard(
            @RequestHeader("id") String userId,
            @RequestParam("centerId") String centerId,
            @RequestParam(value = "startDate", defaultValue = "") String startDate,
            @RequestParam(value = "endDate", defaultValue = "") String endDate
    ) throws JsonProcessingException {
        return new ResponseEntity<>(this.centerService.getCenterDashboard(UUID.fromString(userId), UUID.fromString(centerId), startDate, endDate), HttpStatus.OK);
    }
}
