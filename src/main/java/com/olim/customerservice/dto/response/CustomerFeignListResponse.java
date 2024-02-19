package com.olim.customerservice.dto.response;

import com.olim.customerservice.entity.Customer;

import java.util.ArrayList;
import java.util.List;

public record CustomerFeignListResponse(
        Boolean success,
        Long total,
        List<CustomerFeignResponse> hits
) {
    public static CustomerFeignListResponse makeDto(List<Customer> customers) {
        List<CustomerFeignResponse> customerFeignResponses = new ArrayList<>();
        for (Customer customer : customers) {
            customerFeignResponses.add(CustomerFeignResponse.makeDto(customer));
        }
        return new CustomerFeignListResponse(customers.size() == 1 ? true : false, (long) customers.size(), customerFeignResponses);
    }
}
