package com.olim.customerservice.dto.response;

import com.olim.customerservice.entity.Customer;
import com.olim.customerservice.enumeration.Gender;

import javax.swing.text.DateFormatter;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public record CustomerGetResponse(
        Long id,
        String name,
        Gender gender,
        String birthDate,
        String phoneNumber,
        String address,
        Long instructorId

) {
    public static CustomerGetResponse makeDto(Customer customer) {
        CustomerGetResponse response = new CustomerGetResponse(
                customer.getId(),
                customer.getName(),
                customer.getGender(),
                customer.getBirthDate().format(DateTimeFormatter.ISO_DATE),
                customer.getPhoneNumber(),
                customer.getAddress(),
                customer.getInstructor().getId()
        );
        return response;
    }
}
