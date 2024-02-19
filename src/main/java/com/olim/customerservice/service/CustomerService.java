package com.olim.customerservice.service;

import com.olim.customerservice.dto.request.CustomerEnrollRequest;
import com.olim.customerservice.dto.request.CustomerPutProfileRequest;
import com.olim.customerservice.dto.response.CustomerFeignResponse;
import com.olim.customerservice.dto.response.CustomerGetResponse;
import com.olim.customerservice.dto.response.CustomerListResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface CustomerService {
    @Transactional
    String enrollCustomer(CustomerEnrollRequest customerEnrollRequest, UUID userId);
    @Transactional
    CustomerListResponse getListCustomer(String centerId, UUID userId, int page, int count, String sortBy, String keyword, Boolean orderByDesc);
    @Transactional
    String putCustomerProfile(Long customerId, UUID userId, CustomerPutProfileRequest customerPutProfileRequest);
    @Transactional
    CustomerGetResponse getCustomerProfile(Long customerId, UUID userId);
    @Transactional
    String deleteCustomer(Long customerId, UUID userId);
    @Transactional
    Boolean checkPhoneNumber(String phoneNumber, UUID centerId);
    @Transactional
    CustomerFeignResponse getCustomerInfo(UUID userId, Long customerId);
    @Transactional
    CustomerFeignResponse getCustomerInfo(UUID userId, String phoneNumber, String centerId);
}
