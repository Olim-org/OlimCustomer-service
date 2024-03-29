package com.olim.customerservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.olim.customerservice.clients.ReserveClient;
import com.olim.customerservice.clients.UserClient;
import com.olim.customerservice.dto.request.CenterCreateRequest;
import com.olim.customerservice.dto.request.CenterModifyRequest;
import com.olim.customerservice.dto.response.*;
import com.olim.customerservice.entity.Center;
import com.olim.customerservice.entity.Customer;
import com.olim.customerservice.entity.Instructor;
import com.olim.customerservice.enumeration.CenterStatus;
import com.olim.customerservice.enumeration.CustomerStatus;
import com.olim.customerservice.enumeration.VisitRoute;
import com.olim.customerservice.exception.customexception.DataNotFoundException;
import com.olim.customerservice.exception.customexception.PermissionFailException;
import com.olim.customerservice.repository.CenterRepository;
import com.olim.customerservice.repository.CustomerRepository;
import com.olim.customerservice.repository.InstructorRepository;
import com.olim.customerservice.service.CenterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CenterServiceImpl implements CenterService {
    private final CenterRepository centerRepository;
    private final CustomerRepository customerRepository;
    private final InstructorRepository instructorRepository;
    private final UserClient userClient;
    private final ReserveClient reserveClient;
    @Transactional
    @Override
    public CenterCreateResponse createCenter(CenterCreateRequest centerCreateRequest, UUID userId, String token) {
        UserInfoFeignResponse userInfo = this.userClient.getUserInfo(token);
        if (userInfo.role().equals("ROLE_GUEST")) {
            throw new PermissionFailException("회원 인증을 거치지 않은 유저는 센터를 만들 수 없습니다.");
        }
        Center newCenter = Center.builder()
                .name(centerCreateRequest.name())
                .owner(userId)
                .build();
        Center savedCenter = this.centerRepository.save(newCenter);

        Instructor instructor = Instructor.builder()
                .center(savedCenter)
                .address(userInfo.address())
                .phoneNumber(userInfo.phoneNumber())
                .gender(userInfo.gender())
                .name(userInfo.name())
                .birthDate(userInfo.birthDate())
                .owner(userId)
                .centerInstructorId(Long.parseLong("0"))
                .build();
        this.instructorRepository.save(instructor);
        CenterCreateResponse centerCreateResponse = CenterCreateResponse.makeDto(savedCenter);
        return centerCreateResponse;
    }
    @Override
    public CenterGetListResponse getMyCenterList(UUID userId) {
        List<Center> centers = this.centerRepository.findAllByOwnerAndStatusIsNot(userId, CenterStatus.DELETE);
        if (centers.size() == 0) {
            CenterGetListResponse centerGetListResponse = CenterGetListResponse.makeDto(centers);
            return centerGetListResponse;
        }
        CenterGetListResponse centerGetListResponse = CenterGetListResponse.makeDto(centers);
        return centerGetListResponse;
    }
    @Override
    public CenterGetResponse getCenter(UUID userId, UUID centerId) {
        Optional<Center> center = centerRepository.findById(centerId);
        if (!center.isPresent()) {
            throw new DataNotFoundException("해당 센터를 찾을 수 없습니다.");
        }
        if (!center.get().getOwner().equals(userId)) {
            throw new PermissionFailException("해당 센터를 조회할 권한이 없습니다.");
        }
        CenterGetResponse centerGetResponse = CenterGetResponse.makeDto(center.get());
        return centerGetResponse;
    }
    @Transactional
    @Override
    public String updateCenter(UUID userId, UUID centerId, CenterModifyRequest centerModifyRequest) {
        Optional<Center> center = centerRepository.findById(centerId);
        if (!center.isPresent()) {
            throw new DataNotFoundException("해당 아이디의 센터를 찾을 수 없습니다.");
        }
        if (!center.get().getOwner().equals(userId) || center.get().getStatus() == CenterStatus.DELETE) {
            throw new PermissionFailException("해당 센터를 수정할 권한이 없습니다.");
        }
        Center gotCenter = center.get();
        gotCenter.updateCenter(centerModifyRequest);
        centerRepository.save(gotCenter);
        return "성공적으로 " + gotCenter.getName() + " 센터가 수정 되었습니다.";
    }

    @Override
    public String deleteCenter(UUID userId, UUID centerId) {
        Optional<Center> center = centerRepository.findById(centerId);
        if (!center.isPresent()) {
            throw new DataNotFoundException("해당 아이디의 센터를 찾을 수 없습니다.");
        }
        if (!center.get().getOwner().equals(userId) || center.get().getStatus() == CenterStatus.DELETE) {
            throw new PermissionFailException("해당 센터가 이미 삭제 되었거나 삭제할 권한이 없습니다.");
        }
        Center gotCenter = center.get();
        gotCenter.deleteCenter();
        Center deletedCenter = centerRepository.save(gotCenter);
        List<Customer> customers = customerRepository.findAllByCenter(deletedCenter);
        List<Instructor> instructors = instructorRepository.findAllByCenter(deletedCenter);
        for (Customer customer : customers) {
            customer.deletedByCenter();
        }
        for (Instructor instructor : instructors) {
            instructor.deletedByCenter();
        }
        instructorRepository.saveAll(instructors);
        customerRepository.saveAll(customers);
        return "성공적으로 해당 센터가 삭제 되었습니다.";
    }

    @Override
    public CenterFeignResponse getCenterInfo(UUID userId, UUID centerId) {
        Optional<Center> center = centerRepository.findById(centerId);
        if (!center.isPresent()) {
            throw new DataNotFoundException("해당 센터를 찾을 수 없습니다.");
        }
        if (!center.get().getOwner().equals(userId)) {
            throw new PermissionFailException("해당 센터를 조회할 권한이 없습니다.");
        }
        CenterFeignResponse centerFeignResponse = CenterFeignResponse.makeDto(center.get());
        return centerFeignResponse;
    }
    @Override
    public CenterDashBoardResponse getCenterDashboard(UUID userId, UUID centerId, String startDate, String endDate) throws JsonProcessingException {
        Optional<Center> center = centerRepository.findById(centerId);
        if (!center.isPresent()) {
            throw new DataNotFoundException("해당 센터를 찾을 수 없습니다.");
        }
        if (!center.get().getOwner().equals(userId)) {
            throw new PermissionFailException("해당 센터를 조회할 권한이 없습니다.");
        }


        if (startDate.equals("") || endDate.equals("")) {
            startDate = LocalDate.now().minusDays(31).format(DateTimeFormatter.ISO_DATE);
            endDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        } else if (startDate.equals("")) {
            startDate = LocalDate.now().minusDays(31).format(DateTimeFormatter.ISO_DATE);
        } else if (endDate.equals("")) {
            endDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        }
        List<Customer> customers = customerRepository.findAllByCenterAndStatusNotInAndCreatedAtAfterAndCreatedAtBefore(center.get(), List.of(CustomerStatus.DELETE, CustomerStatus.CENTER_DELETED), LocalDateTime.of(LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE), LocalTime.MIN), LocalDateTime.of(LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE), LocalTime.MAX));
        List<Long> customerIds = customers.stream().map(Customer::getId).toList();

        CenterNewCustomerResponse centerNewCustomerResponse = reserveClient.getTicketCustomersIsValid(userId.toString(), centerId.toString(), customerIds);
        List<TicketSalesResponse> ticketSalesResponses = reserveClient.getTicketSales(userId.toString(), centerId.toString(), startDate, endDate);
        Map<VisitRoute, List<Long>> routeTicketSalesResponses = customers.stream().collect(
                Collectors.groupingBy(Customer::getVisitRoute, Collectors.mapping(Customer::getId, Collectors.toList()))
        );
        Map<String, List<Long>> routeAndId = new HashMap<>();
        for (VisitRoute visitRoute : routeTicketSalesResponses.keySet()) {
            routeAndId.put(visitRoute.getKey(), routeTicketSalesResponses.get(visitRoute));
        }
        ObjectMapper objectMapper = new ObjectMapper();
        List<RouteSalseResponse> routeSalseResponses = reserveClient.getRouteTicketSales(userId.toString(), centerId.toString(), objectMapper.writeValueAsString(routeAndId));
        Map<VisitRoute, Long> routeCustomerCount = customers.stream().collect(
                Collectors.groupingBy(Customer::getVisitRoute, Collectors.counting())
        );
        List<CenterVisitRouteResponse> centerVisitRouteResponses = new ArrayList<>();
        for (VisitRoute visitRoute : VisitRoute.values()) {
            CenterVisitRouteResponse centerVisitRouteResponse = CenterVisitRouteResponse.makeDto(visitRoute.getKey(), routeCustomerCount.getOrDefault(visitRoute, 0L).toString());
            centerVisitRouteResponses.add(centerVisitRouteResponse);
        }
        CenterDashBoardResponse centerDashboardResponse = CenterDashBoardResponse.makeDto(
                centerNewCustomerResponse,
                ticketSalesResponses,
                routeSalseResponses,
                centerVisitRouteResponses
        );
        return centerDashboardResponse;
    }
}
