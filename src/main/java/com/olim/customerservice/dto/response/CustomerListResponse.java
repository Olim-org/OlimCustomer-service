package com.olim.customerservice.dto.response;

import com.olim.customerservice.entity.Customer;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public record CustomerListResponse(
        Long total,
        List<CustomerGetResponse> hits
) {
    public static CustomerListResponse makeDto(Page<Customer> customerPage) {
        List<CustomerGetResponse> responses = new ArrayList<>();
        for (Customer customer : customerPage.getContent()) {
            CustomerGetResponse customerGetResponse = CustomerGetResponse.makeDto(customer);
            responses.add(customerGetResponse);
        }
        return new CustomerListResponse(customerPage.getTotalElements(), responses);
    }
}
