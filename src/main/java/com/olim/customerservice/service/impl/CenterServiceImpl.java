package com.olim.customerservice.service.impl;

import com.olim.customerservice.clients.UserClient;
import com.olim.customerservice.dto.request.CenterCreateRequest;
import com.olim.customerservice.dto.response.CenterCreateResponse;
import com.olim.customerservice.dto.response.CenterGetListResponse;
import com.olim.customerservice.dto.response.UserInfoFeignResponse;
import com.olim.customerservice.entity.Center;
import com.olim.customerservice.entity.Customer;
import com.olim.customerservice.entity.Instructor;
import com.olim.customerservice.enumeration.CustomerRole;
import com.olim.customerservice.enumeration.Gender;
import com.olim.customerservice.exception.customexception.DataNotFoundException;
import com.olim.customerservice.exception.customexception.DuplicateException;
import com.olim.customerservice.exception.customexception.PermissionFailException;
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
import java.time.format.DateTimeFormatter;
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
    private final UserClient userClient;
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
        List<Center> centers = this.centerRepository.findAllByOwner(userId);
        if (centers.size() == 0) {
            throw new DataNotFoundException("해당 유저가 소유 중인 센터가 존재하지 않습니다");
        }
        CenterGetListResponse centerGetListResponse = CenterGetListResponse.makeDto(centers);
        return centerGetListResponse;
    }
}
