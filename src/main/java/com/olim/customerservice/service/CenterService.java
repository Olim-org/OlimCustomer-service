package com.olim.customerservice.service;

import com.olim.customerservice.dto.request.CenterCreateRequest;
import com.olim.customerservice.dto.request.CenterModifyRequest;
import com.olim.customerservice.dto.response.CenterCreateResponse;
import com.olim.customerservice.dto.response.CenterGetListResponse;
import com.olim.customerservice.dto.response.CenterFeignResponse;
import com.olim.customerservice.dto.response.CenterGetResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface CenterService {
    @Transactional
    CenterCreateResponse createCenter(CenterCreateRequest centerCreateRequest, UUID userId, String token);
    @Transactional
    CenterGetListResponse getMyCenterList(UUID userId);
    @Transactional
    CenterGetResponse getCenter(UUID userId, UUID centerId);
    @Transactional
    String updateCenter(UUID userId, UUID centerId, CenterModifyRequest centerModifyRequest);
    @Transactional
    String deleteCenter(UUID userId, UUID centerId);
    @Transactional
    CenterFeignResponse getCenterInfo(UUID userId, UUID centerId);
}
