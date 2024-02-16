package com.olim.customerservice.service.impl;

import com.olim.customerservice.dto.request.InstructorCreateRequest;
import com.olim.customerservice.dto.request.InstructorModifyRequest;
import com.olim.customerservice.dto.response.InstructorGetListByCenterResponse;
import com.olim.customerservice.entity.Center;
import com.olim.customerservice.entity.Customer;
import com.olim.customerservice.entity.Instructor;
import com.olim.customerservice.enumeration.CenterStatus;
import com.olim.customerservice.enumeration.CustomerRole;
import com.olim.customerservice.enumeration.InstructorStatus;
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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
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

        Instructor instructor = Instructor.builder()
                .center(center.get())
                .address(instructorCreateRequest.address())
                .phoneNumber(instructorCreateRequest.phoneNumber())
                .gender(instructorCreateRequest.gender())
                .name(instructorCreateRequest.name())
                .birthDate(LocalDate.parse(instructorCreateRequest.birthDate(), DateTimeFormatter.ISO_DATE))
                .owner(userId)
                .centerInstructorId(getLastInstructorNumber(center.get())+1)
                .build();
        this.instructorRepository.save(instructor);

        return "성공적으로 " + instructor.getName() + "님이 " + "강사로 등록 되었습니다.";
    }
    @Transactional
    @Override
    public InstructorGetListByCenterResponse getInstructorListByCenter(String centerId, UUID userId, String keyword, int page, int count) {
        if (centerId != null) {
            UUID centerUUID = UUID.fromString(centerId);
            Optional<Center> center = this.centerRepository.findById(centerUUID);
            if (!center.isPresent()) {
                throw new DataNotFoundException("해당 센터는 존재하지 않습니다.");
            }
            if (!center.get().getOwner().equals(userId)) {
                throw new PermissionFailException("센터 점주가 아닙니다.");
            }
            Sort sort = Sort.by("name").ascending();

            Pageable pageable = PageRequest.of(page, count, sort);

            Page<Instructor> instructors = this.instructorRepository.findAllByCenterAndStatusNotInNameContaining(center.get(), List.of(InstructorStatus.CENTER_DELETED, InstructorStatus.DELETE), keyword, pageable);
            InstructorGetListByCenterResponse instructorGetListByCenterResponse =
                    InstructorGetListByCenterResponse.makeDto(instructors);
            return instructorGetListByCenterResponse;
        } else {
            List<Center> centers = this.centerRepository.findAllByOwnerAndStatusIsNot(userId, CenterStatus.DELETE);
            if (centers.isEmpty()) {
                throw new DataNotFoundException("해당 유저의 센터가 존재하지 않습니다.");
            }
            Sort sort = Sort.by("name").ascending();

            Pageable pageable = PageRequest.of(page, count, sort);
            Page<Instructor> instructors = this.instructorRepository.findAllByOwnerAndStatusNotInAndNameContaining(userId, List.of(InstructorStatus.CENTER_DELETED, InstructorStatus.DELETE), keyword, pageable);

            InstructorGetListByCenterResponse instructorGetListByCenterResponse =
                    InstructorGetListByCenterResponse.makeDto(instructors);
            return instructorGetListByCenterResponse;
        }

    }
    @Transactional
    @Override
    public String updateInstructor(UUID userId, Long instructorId, InstructorModifyRequest instructorModifyRequest) {
        if (instructorId == null) {
            throw new DataNotFoundException("해당 아이디의 강사를 찾을 수 없습니다.");
        }
        if (instructorModifyRequest.status().getKey().equals("DELETE")) {
            throw new PermissionFailException("강사를 삭제할 수 없습니다. 삭제는 삭제 API를 이용해주세요.");
        }
        if (instructorModifyRequest.status().getKey().equals("CENTER_DELETED")) {
            throw new PermissionFailException("해당 방식은 허용되지 않습니다.");
        }
        Optional<Instructor> instructor = instructorRepository.findById(instructorId);
        if (!instructor.isPresent()) {
            throw new DataNotFoundException("해당 아이디의 강사를 찾을 수 없습니다.");
        }
        if (!instructor.get().getOwner().equals(userId)) {
            throw new PermissionFailException("해당 강사를 수정할 수 있는 권한이 없습니다.");
        }
        Instructor gotInstructor = instructor.get();
        gotInstructor.updateInstructor(instructorModifyRequest);
        instructorRepository.save(gotInstructor);
        return "성공적으로 강사 " + gotInstructor.getName() + "님의 정보가 수정 되었습니다.";
    }

    @Override
    public String deleteInstructor(UUID userId, Long instructorId) {
        if (instructorId == null) {
            throw new DataNotFoundException("해당 아이디의 강사를 찾을 수 없습니다.");
        }
        Optional<Instructor> instructor = instructorRepository.findById(instructorId);
        if (!instructor.isPresent()) {
            throw new DataNotFoundException("해당 아이디의 강사를 찾을 수 없습니다.");
        }
        if (!instructor.get().getOwner().equals(userId) || instructor.get().getStatus() == InstructorStatus.DELETE) {
            throw new PermissionFailException("해당 강사가 이미 삭제 되었거나 삭제할 수 있는 권한이 없습니다.");
        }
        Instructor gotInstructor = instructor.get();
        gotInstructor.deleteInstructor();
        instructorRepository.save(gotInstructor);
        return "성공적으로 강사 " + gotInstructor.getName() + "님이 해임 되었습니다.";
    }

    private Long getLastInstructorNumber(Center center) {
        Instructor instructor = instructorRepository.findTopByCenterOrderByCenterInstructorIdDesc(center);
        if (instructor == null) {
            return Long.parseLong("0");
        }
        return instructor.getCenterInstructorId();
    }
}
