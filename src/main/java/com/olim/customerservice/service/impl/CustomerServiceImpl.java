package com.olim.customerservice.service.impl;

import com.olim.customerservice.dto.request.CustomerEnrollRequest;
import com.olim.customerservice.dto.request.CustomerPutProfileRequest;
import com.olim.customerservice.dto.response.CustomerGetResponse;
import com.olim.customerservice.dto.response.CustomerListResponse;
import com.olim.customerservice.entity.Center;
import com.olim.customerservice.entity.Customer;
import com.olim.customerservice.entity.Instructor;
import com.olim.customerservice.enumeration.CenterStatus;
import com.olim.customerservice.enumeration.CustomerRole;
import com.olim.customerservice.enumeration.CustomerStatus;
import com.olim.customerservice.exception.customexception.DataNotFoundException;
import com.olim.customerservice.exception.customexception.PermissionFailException;
import com.olim.customerservice.repository.CenterRepository;
import com.olim.customerservice.repository.CustomerRepository;
import com.olim.customerservice.repository.InstructorRepository;
import com.olim.customerservice.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final InstructorRepository instructorRepository;
    private final CenterRepository centerRepository;
    @Transactional
    @Override
    public String enrollCustomer(CustomerEnrollRequest customerEnrollRequest, UUID userId) {
        Optional<Center> center = this.centerRepository.findById(customerEnrollRequest.centerId());
        if (!center.isPresent()) {
            throw new DataNotFoundException("해당 센터를 찾을 수 없습니다.");
        }
        if (!center.get().getOwner().equals(userId)) {
            throw new DataNotFoundException("센터 관리자 또는 점주가 아닙니다.");
        }
        Instructor gotInstructor = null;
        if (customerEnrollRequest.instructorId() != null) {
            Optional<Instructor> instructor = this.instructorRepository.findById(customerEnrollRequest.instructorId());
            if (!instructor.isPresent()) {
                throw new DataNotFoundException("해당 강사를 찾을 수 없습니다.");
            }
            gotInstructor = instructor.get();
        }
        Customer customer = Customer.builder()
                .name(customerEnrollRequest.name())
                .gender(customerEnrollRequest.gender())
                .address(customerEnrollRequest.address())
                .birthDate(LocalDate.parse(customerEnrollRequest.birthDate(), DateTimeFormatter.ISO_DATE))
                .center(center.get())
                .phoneNumber(customerEnrollRequest.phoneNumber())
                .centerCustomerId(getLastCustomerNumber(center.get()) + 1)
                .owner(userId)
                .instructor(gotInstructor).build();
        Customer savedCustomer = this.customerRepository.save(customer);
        return "성공적으로 " + customerEnrollRequest.name() +" 회원이 등록 되었습니다.";
    }
    @Transactional
    @Override
    public CustomerListResponse getListCustomer(
            String centerId,
            UUID userId,
            int page,
            int count,
            String sortBy,
            String keyword,
            Boolean orderByDesc) {
        if (centerId != null) {
            UUID centerUUID = UUID.fromString(centerId);
            Optional<Center> center = this.centerRepository.findById(centerUUID);
            if (!center.isPresent()) {
                throw new DataNotFoundException("해당 ID의 센터가 존재하지 않습니다.");
            }

            if (!center.get().getOwner().equals(userId)) {
                throw new PermissionFailException("회원을 조회할 권한이 없습니다.");
            }
            if (!(sortBy.equals("name") || sortBy.equals("cAt"))) {
                sortBy = "name";
            }
            Sort sort = (orderByDesc) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

            Pageable pageable = PageRequest.of(page, count, sort);

            Page<Customer> customers = this.customerRepository.findAllByCenterAndRoleAndNameContaining(center.get(), CustomerRole.CUSTOMER_USER, keyword, pageable);
            CustomerListResponse customerListResponse = CustomerListResponse.makeDto(customers);
            return customerListResponse;
        } else {
            List<Center> centers = this.centerRepository.findAllByOwnerAndStatusIsNot(userId, CenterStatus.DELETE);
            if (centers.isEmpty()) {
                throw new DataNotFoundException("해당 유저의 센터가 존재하지 않습니다.");
            }
            if (!(sortBy.equals("name") || sortBy.equals("cAt"))) {
                sortBy = "name";
            }
            Sort sort = (orderByDesc) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

            Pageable pageable = PageRequest.of(page, count, sort);

            Page<Customer> customers = this.customerRepository.findAllByOwnerAndRoleAndStatusNotInAndNameContaining(userId, CustomerRole.CUSTOMER_USER, List.of(CustomerStatus.CENTER_DELETED, CustomerStatus.DELETE), keyword, pageable);

            CustomerListResponse customerListResponse = CustomerListResponse.makeDto(customers);
            return customerListResponse;
        }
    }
    @Transactional
    @Override
    public String putCustomerProfile(Long customerId, UUID userId, CustomerPutProfileRequest customerPutProfileRequest) {
        Optional<Customer> customer = this.customerRepository.findById(customerId);
        if (!customer.isPresent()) {
            throw new DataNotFoundException("해당 아이디의 유저를 찾을 수 없습니다.");
        }
        if (!customer.get().getOwner().equals(userId)) {
            throw new PermissionFailException("해당 아이디의 고객 프로필을 등록할 권한이 없습니다.");
        }
        Customer gotCustomer = customer.get();
        Optional<Instructor> instructor = null;
        if (customerPutProfileRequest.instructorId() != null) {
            instructor = this.instructorRepository.findById(customerPutProfileRequest.instructorId());
            if (!instructor.isPresent()) {
                throw new DataNotFoundException("해당 아이디의 강사를 찾을 수 없습니다.");
            }
            if (!instructor.get().getOwner().equals(userId)) {
                throw new DataNotFoundException("해당 강사는 해당 센터에 소속된 강사가 아닙니다.");
            }
        }
        gotCustomer.updateProfile(customerPutProfileRequest, instructor != null ? instructor.get() : null);
        this.customerRepository.save(gotCustomer);
        return "성공적으로 " + gotCustomer.getName() + "님의 프로필이 수정 되었습니다.";
    }
    @Transactional
    @Override
    public CustomerGetResponse getCustomerProfile(Long customerId, UUID userId) {
        Optional<Customer> customer = this.customerRepository.findById(customerId);
        if (!customer.isPresent()) {
            throw new DataNotFoundException("해당 아이디의 유저를 찾을 수 없습니다.");
        }
        if (!customer.get().getOwner().equals(userId)) {
            throw new PermissionFailException("해당 아이디의 고객 프로필을 조회할 권한이 없습니다.");
        }
        Customer gotCustomer = customer.get();
        CustomerGetResponse customerGetResponse = CustomerGetResponse.makeDto(gotCustomer);
        return customerGetResponse;
    }

    private Long getLastCustomerNumber(Center center) {
        Customer customer = customerRepository.findTopByCenterOrderByCenterCustomerIdDesc(center);
        if (customer == null) {
            return Long.parseLong("0");
        }
        return customer.getCenterCustomerId();
    }
}
