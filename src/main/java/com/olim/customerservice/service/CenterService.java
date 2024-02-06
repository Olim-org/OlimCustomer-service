package com.olim.customerservice.service;

import com.olim.customerservice.dto.request.CenterCreateRequest;
import com.olim.customerservice.dto.response.CenterCreateResponse;
import com.olim.customerservice.dto.response.CenterGetResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface CenterService {
    @Transactional
    CenterCreateResponse createCenter(CenterCreateRequest centerCreateRequest, UUID userId);
    @Transactional
    CenterGetResponse getMyCenter(UUID userId);
}
