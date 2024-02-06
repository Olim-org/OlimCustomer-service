package com.olim.customerservice.service;

import com.olim.customerservice.dto.request.CustomerEnrollRequest;
import com.olim.customerservice.dto.response.CustomerListResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface CustomerService {
    @Transactional
    String enrollCustomer(CustomerEnrollRequest customerEnrollRequest, UUID userId);
    @Transactional
    CustomerListResponse getListCustomer(UUID centerId, UUID userId);
}
