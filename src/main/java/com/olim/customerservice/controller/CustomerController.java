package com.olim.customerservice.controller;

import com.olim.customerservice.dto.request.CustomerEnrollRequest;
import com.olim.customerservice.dto.response.CustomerListResponse;
import com.olim.customerservice.service.CustomerService;
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
@RequiredArgsConstructor
@RequestMapping("/customer-service/customer")
@Tag(name = "Customer", description = "Customer API 구성")
@Validated
@Slf4j
public class CustomerController {
    private final CustomerService customerService;
    @PostMapping("/enroll")
    @Operation(description = "회원 등록하기")
    @Parameters({
            @Parameter(name = "userId", description = "액세스 토큰 아이디", required = true, in = ParameterIn.HEADER)
    })
    public ResponseEntity<String> enrollCustomer(@RequestBody CustomerEnrollRequest customerEnrollRequest, @RequestHeader("id") String userId) {
        return new ResponseEntity<>(this.customerService.enrollCustomer(customerEnrollRequest, UUID.fromString(userId)), HttpStatus.OK);
    }
    @GetMapping("/list")
    @Operation(description = "센터 내 회원 목록 불러오기")
    @Parameters({
            @Parameter(name = "centerId", description = "센터 UUID", required = false, in = ParameterIn.QUERY, example = "asdf-qr-xcv-daf"),
            @Parameter(name = "userId", description = "액세스 토큰 아이디", required = true, in = ParameterIn.HEADER, example = "asdf-qr-xcv-daf"),
            @Parameter(name = "page", description = "페이지", in = ParameterIn.QUERY, required = false, example = "0"),
            @Parameter(name = "count", description = "페이지 내 아이템 수", in = ParameterIn.QUERY, required = false, example = "20"),
            @Parameter(name = "sortBy", description = "정렬 기준", in = ParameterIn.QUERY, example = "name"),
            @Parameter(name = "keyword", description = "검색어", in = ParameterIn.QUERY, example = "김"),
            @Parameter(name = "orderByDesc", description = "내림차순", in = ParameterIn.QUERY, example = "true")
    })
    public ResponseEntity<CustomerListResponse> getListCustomer(
            @RequestParam(value = "centerId", required = false) String centerId,
            @RequestHeader("id") String userId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "count", defaultValue = "50") int count,
            @RequestParam(value = "sortBy", defaultValue = "title") String sortBy,
            @RequestParam(value = "keyword", defaultValue = "") String keyword,
            @RequestParam(value = "orderByDesc", defaultValue = "true") Boolean orderByDesc
            ) {
        return new ResponseEntity<>(this.customerService.getListCustomer(UUID.fromString(centerId), UUID.fromString(userId), page, count, sortBy, keyword, orderByDesc), HttpStatus.OK);
    }
}
