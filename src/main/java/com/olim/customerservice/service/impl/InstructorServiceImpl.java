package com.olim.customerservice.service.impl;

import com.olim.customerservice.dto.request.InstructorCreateRequest;
import com.olim.customerservice.dto.response.InstructorGetListByCenterResponse;
import com.olim.customerservice.entity.Center;
import com.olim.customerservice.entity.Customer;
import com.olim.customerservice.entity.Instructor;
import com.olim.customerservice.enumeration.CustomerRole;
import com.olim.customerservice.exception.customexception.DataNotFoundException;
import com.olim.customerservice.exception.customexception.DuplicateException;
import com.olim.customerservice.exception.customexception.PermissionFailException;
import com.olim.customerservice.repository.CenterRepository;
import com.olim.customerservice.repository.CustomerRepository;
import com.olim.customerservice.repository.InstructorRepository;
import com.olim.customerservice.service.InstructorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class InstructorServiceImpl implements InstructorService {
    private final InstructorRepository instructorRepository;
    private final CenterRepository centerRepository;
    private final CustomerRepository customerRepository;
    @Transactional
    @Override
    public String createInstructor(UUID userId, InstructorCreateRequest instructorCreateRequest) {
        Optional<Center> center = this.centerRepository.findById(instructorCreateRequest.centerId());
        if (!center.isPresent()) {
            throw new DataNotFoundException("해당 센터를 찾을 수 없습니다.");
        }
        if (!center.get().getOwner().equals(userId)) {
            throw new PermissionFailException("센터 점주가 아닙니다.");
        }
        Optional<Customer> customer = this.customerRepository.findByCenterAndId(center.get(), instructorCreateRequest.userId());
        if (customer.isPresent()) {
            Customer gotCustomer = customer.get();
            gotCustomer.updateRole(CustomerRole.CUSTOMER_INSTRUCTOR);
            this.customerRepository.save(gotCustomer);
        } else {
            throw new DataNotFoundException("해당 고객을 찾을 수 없습니다.");
        }
        if (instructorRepository.findByCenterAndMyCustomer(center.get(), customer.get()).isPresent()) {
            throw new DuplicateException("해당 고객은 이미 강사입니다.");
        }
        Customer gotCustomer = customer.get();
        gotCustomer.updateRole(CustomerRole.CUSTOMER_INSTRUCTOR);
        Customer savedCustomer = customerRepository.save(gotCustomer);
        Instructor instructor = Instructor.builder()
                .centerInstructorId(getLastInstructorNumber(center.get()) + 1)
                .center(center.get())
                .customer(savedCustomer)
                .build();
        this.instructorRepository.save(instructor);

        return "성공적으로 강사가 등록 되었습니다.";
    }
    @Transactional
    @Override
    public InstructorGetListByCenterResponse getInstructorListByCenter(UUID centerId, UUID userId, String keyword, int page, int count) {
        Optional<Center> center = this.centerRepository.findById(centerId);
        if (!center.isPresent()) {
            throw new DataNotFoundException("해당 센터는 존재하지 않습니다.");
        }
        if (!center.get().getOwner().equals(userId)) {
            throw new PermissionFailException("센터 관리자 또는 점주가 아닙니다.");
        }
        Sort sort = Sort.by("name").ascending();

        Pageable pageable = PageRequest.of(page, count, sort);

        Page<Instructor> instructors = this.instructorRepository.findAllByCenterAndNameStartingWith(center.get(), keyword, pageable);
        InstructorGetListByCenterResponse instructorGetListByCenterResponse =
                InstructorGetListByCenterResponse.makeDto(instructors.getContent());
        return instructorGetListByCenterResponse;
    }
    private Long getLastInstructorNumber(Center center) {
        Instructor instructor = instructorRepository.findTopByCenterOrderByCenterInstructorIdDesc(center);
        return instructor.getCenterInstructorId();
    }
}
