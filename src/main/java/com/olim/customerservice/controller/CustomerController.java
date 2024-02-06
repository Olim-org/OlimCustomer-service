package com.olim.customerservice.controller;

import com.olim.customerservice.dto.request.CustomerEnrollRequest;
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
}
