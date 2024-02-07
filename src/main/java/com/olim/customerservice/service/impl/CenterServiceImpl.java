package com.olim.customerservice.service.impl;

import com.olim.customerservice.dto.request.CenterCreateRequest;
import com.olim.customerservice.dto.response.CenterCreateResponse;
import com.olim.customerservice.dto.response.CenterGetListResponse;
import com.olim.customerservice.entity.Center;
import com.olim.customerservice.entity.Customer;
import com.olim.customerservice.entity.Instructor;
import com.olim.customerservice.enumeration.CustomerRole;
import com.olim.customerservice.enumeration.Gender;
import com.olim.customerservice.exception.customexception.DataNotFoundException;
import com.olim.customerservice.exception.customexception.DuplicateException;
import com.olim.customerservice.repository.CenterRepository;
import com.olim.customerservice.repository.CustomerRepository;
import com.olim.customerservice.repository.InstructorRepository;
import com.olim.customerservice.service.CenterService;
import com.olim.customerservice.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CenterServiceImpl implements CenterService {
    private final CenterRepository centerRepository;
    private final CustomerRepository customerRepository;
    private final InstructorRepository instructorRepository;

    @Transactional
    @Override
    public CenterCreateResponse createCenter(CenterCreateRequest centerCreateRequest, UUID userId) {
        Center newCenter = Center.builder()
                .name(centerCreateRequest.name())
                .owner(userId)
                .build();
        Center savedCenter = this.centerRepository.save(newCenter);
        Customer customer = Customer.builder()
                .center(savedCenter)
                .centerCustomerId(Long.parseLong("0"))
                .address("미정")
                .phoneNumber("01000000000")
                .gender(Gender.MALE)
                .name("관리자")
                .birthDate(LocalDate.now())
                .instructor(null)
                .build();
        customer.updateRole(CustomerRole.CUSTOMER_ADMIN);
        Customer savedCustomer = this.customerRepository.save(customer);
        Instructor instructor = Instructor.builder()
                .center(savedCenter)
                .centerInstructorId(Long.parseLong("0"))
                .customer(savedCustomer)
                .build();
        this.instructorRepository.save(instructor);
        CenterCreateResponse centerCreateResponse = CenterCreateResponse.makeDto(savedCenter);
        return centerCreateResponse;
    }

    @Override
    public CenterGetListResponse getMyCenterList(UUID userId) {
        List<Center> centers = this.centerRepository.findAllByOwner(userId);
        if (centers.size() == 0) {
            throw new DataNotFoundException("해당 유저가 소유 중인 센터가 존재하지 않습니다");
        }
        CenterGetListResponse centerGetListResponse = CenterGetListResponse.makeDto(centers);
        return centerGetListResponse;
    }
}
