package com.olim.customerservice.dto.response;

import com.olim.customerservice.entity.Customer;

import java.util.ArrayList;
import java.util.List;

public record CustomerListResponse(
        int total,
        List<CustomerGetResponse> hits
) {
    public static CustomerListResponse makeDto(List<Customer> customers) {
        List<CustomerGetResponse> responses = new ArrayList<>();
        for (Customer customer : customers) {
            CustomerGetResponse customerGetResponse = CustomerGetResponse.makeDto(customer);
            responses.add(customerGetResponse);
        }
        return new CustomerListResponse(responses.size(), responses);
    }
}
