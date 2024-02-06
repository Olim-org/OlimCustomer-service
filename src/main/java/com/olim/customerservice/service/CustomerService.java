package com.olim.customerservice.service;

import com.olim.customerservice.dto.request.CustomerEnrollRequest;
import jakarta.transaction.Transactional;

import java.util.UUID;

public interface CustomerService {
    @Transactional
    String enrollCustomer(CustomerEnrollRequest customerEnrollRequest, UUID userId);
}
