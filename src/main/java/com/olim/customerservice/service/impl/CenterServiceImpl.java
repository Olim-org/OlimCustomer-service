package com.olim.customerservice.service.impl;

import com.olim.customerservice.clients.UserClient;
import com.olim.customerservice.dto.request.CenterCreateRequest;
import com.olim.customerservice.dto.request.CenterModifyRequest;
import com.olim.customerservice.dto.response.CenterCreateResponse;
import com.olim.customerservice.dto.response.CenterGetListResponse;
import com.olim.customerservice.dto.response.CenterFeignResponse;
import com.olim.customerservice.dto.response.UserInfoFeignResponse;
import com.olim.customerservice.entity.Center;
import com.olim.customerservice.entity.Instructor;
import com.olim.customerservice.enumeration.CenterStatus;
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

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
        List<Center> centers = this.centerRepository.findAllByOwnerAndStatusIsNot(userId, CenterStatus.DELETE);
        if (centers.size() == 0) {
            CenterGetListResponse centerGetListResponse = CenterGetListResponse.makeDto(centers);
            return centerGetListResponse;
        }
        CenterGetListResponse centerGetListResponse = CenterGetListResponse.makeDto(centers);
        return centerGetListResponse;
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
        centerRepository.save(gotCenter);
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
}
