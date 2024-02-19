package com.olim.customerservice.controller;

import com.olim.customerservice.dto.request.CustomerEnrollRequest;
import com.olim.customerservice.dto.request.CustomerPutProfileRequest;
import com.olim.customerservice.dto.response.CenterFeignResponse;
import com.olim.customerservice.dto.response.CustomerFeignResponse;
import com.olim.customerservice.dto.response.CustomerGetResponse;
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
        return new ResponseEntity<>(this.customerService.getListCustomer(centerId, UUID.fromString(userId), page, count, sortBy, keyword, orderByDesc), HttpStatus.OK);
    }
    @PutMapping("/profile/{customerId}")
    @Operation(description = "센터 내 고객의 프로필 등록 (추가 정보 기입)")
    @Parameters({
            @Parameter(name = "customerId", description = "프로필을 등록할 고객의 id", in = ParameterIn.PATH, example = "5"),
            @Parameter(name = "userId", description = "액세스 토큰 아이디", in = ParameterIn.HEADER)
    })
    public ResponseEntity<String> putCustomerProfile(
            @PathVariable Long customerId,
            @RequestHeader("id") String userId,
            @RequestBody CustomerPutProfileRequest customerPutProfileRequest
    ) {
        return new ResponseEntity<>(this.customerService.putCustomerProfile(customerId, UUID.fromString(userId), customerPutProfileRequest), HttpStatus.OK);
    }
    @GetMapping("/profile/{customerId}")
    @Operation(description = "센터 내 고객의 프로필 조회 (추가 정보 기입)")
    @Parameters({
            @Parameter(name = "customerId", description = "프로필을 등록할 고객의 id", in = ParameterIn.PATH, example = "5"),
            @Parameter(name = "userId", description = "액세스 토큰 아이디", in = ParameterIn.HEADER)
    })
    public ResponseEntity<CustomerGetResponse> getCustomerProfile(
            @PathVariable Long customerId,
            @RequestHeader("id") String userId
    ) {
        return new ResponseEntity<>(this.customerService.getCustomerProfile(customerId, UUID.fromString(userId)), HttpStatus.OK);
    }
    @DeleteMapping("/{customerId}")
    @Operation(description = "센터 내 고객 삭제하기")
    @Parameters({
            @Parameter(name = "userId", description = "액세스 토큰 아이디", required = true, in = ParameterIn.HEADER),
            @Parameter(name = "customerId", description = "고객 UID", required = true, in = ParameterIn.PATH)
    })
    public ResponseEntity<String> deleteCustomer(
            @RequestHeader("id") String userId,
            @PathVariable Long customerId
    ) {
        return new ResponseEntity<>(this.customerService.deleteCustomer(customerId, UUID.fromString(userId)), HttpStatus.OK);
    }
    @GetMapping("/check-phone/{phoneNumber}")
    @Operation(description = "전화번호 중복 확인")
    @Parameters({
            @Parameter(name = "phoneNumber", description = "전화번호", required = true, in = ParameterIn.PATH),
            @Parameter(name = "centerId", description = "센터 UUID", required = true, in = ParameterIn.QUERY)
    })
    public ResponseEntity<Boolean> checkPhoneNumber(
            @PathVariable String phoneNumber,
            @RequestParam("centerId") String centerId)
    {
        return new ResponseEntity<>(this.customerService.checkPhoneNumber(phoneNumber, UUID.fromString(centerId)), HttpStatus.OK);
    }
    @Operation(description = "고객 정보 가져오기 Feign 클라이언트 용 with customerId")
    @GetMapping("/info")
    public CustomerFeignResponse getCenterInfo(@RequestHeader("id") String userId, @RequestParam(value = "customerId") Long customerId) {
        return this.customerService.getCustomerInfo(UUID.fromString(userId), customerId);
    }
    @Operation(description = "고객 정보 가져오기 Feign 클라이언트 용 with 휴대폰 번호")
    @GetMapping("/info")
    public CustomerFeignResponse getCenterInfo(
            @RequestHeader("id") String userId,
            @RequestParam(value = "phoneNumber") String phoneNumber,
            @RequestParam(value = "centerId") String centerId) {
        return this.customerService.getCustomerInfo(UUID.fromString(userId), phoneNumber, centerId);
    }
}
