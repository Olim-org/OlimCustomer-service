package com.olim.customerservice.dto.response;

import com.olim.customerservice.entity.Center;
import com.olim.customerservice.entity.Customer;
import com.olim.customerservice.enumeration.Gender;
import com.olim.customerservice.enumeration.VisitRoute;

import javax.swing.text.DateFormatter;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

public record CustomerGetResponse(
        Long uid,
        String centerName,
        Long id,
        String name,
        Gender gender,
        String birthDate,
        String phoneNumber,
        String address,
        Long instructorId,
        String instructorName,
        List<VisitRoute> visitRoute,
        String healthExp,
        String purpose,
        String diseases,
        String pregnant,
        String lifeHabit,
        String desiredTimeSlot,
        Boolean kakaoTalkAlert

) {
    public static CustomerGetResponse makeDto(Customer customer) {
        CustomerGetResponse response = new CustomerGetResponse(
                customer.getId(),
                customer.getCenter().getName(),
                customer.getCenterCustomerId(),
                customer.getName(),
                customer.getGender(),
                customer.getBirthDate().format(DateTimeFormatter.ISO_DATE),
                customer.getPhoneNumber(),
                customer.getAddress(),
                customer.getInstructor() != null ? customer.getInstructor().getId() : null,
                customer.getInstructor() != null ? customer.getInstructor().getName() : null,
                customer.getVisitRoute(),
                customer.getHealthExp(),
                customer.getPurpose(),
                customer.getDiseases(),
                customer.getPregnant(),
                customer.getLifeHabit(),
                customer.getDesiredTimeSlot(),
                customer.getKakaoTalkAlert()
        );
        return response;
    }
}
