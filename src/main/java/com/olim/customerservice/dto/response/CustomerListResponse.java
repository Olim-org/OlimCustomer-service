package com.olim.customerservice.dto.response;

import com.olim.customerservice.entity.Customer;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public record CustomerListResponse(
        @Schema(description = "총 고객 수", example = "10")
        Long total,
        @Schema(description = "고객 목록")
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
