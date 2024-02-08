package com.olim.customerservice.clients;

import com.olim.customerservice.dto.response.UserInfoFeignResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = "auth-service", path = "/auth-service/user")
public interface UserClient {
    String AUTHORIZATION = "Authorization";
    @GetMapping("/info")
    UserInfoFeignResponse getUserInfo(@RequestHeader(AUTHORIZATION) String token);
}