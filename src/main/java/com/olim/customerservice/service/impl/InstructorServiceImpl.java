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

            Page<Instructor> instructors = this.instructorRepository.findAllByCenterAndNameContaining(center.get(), keyword, pageable);
            InstructorGetListByCenterResponse instructorGetListByCenterResponse =
                    InstructorGetListByCenterResponse.makeDto(instructors);
            return instructorGetListByCenterResponse;
        } else {
            List<Center> centers = this.centerRepository.findAllByOwner(userId);
            if (centers.isEmpty()) {
                throw new DataNotFoundException("해당 유저의 센터가 존재하지 않습니다.");
            }
            Sort sort = Sort.by("name").ascending();

            Pageable pageable = PageRequest.of(page, count, sort);
            Page<Instructor> instructors = this.instructorRepository.findAllByOwnerAndNameContaining(userId, keyword, pageable);

            InstructorGetListByCenterResponse instructorGetListByCenterResponse =
                    InstructorGetListByCenterResponse.makeDto(instructors);
            return instructorGetListByCenterResponse;
        }

    }
    private Long getLastInstructorNumber(Center center) {
        Instructor instructor = instructorRepository.findTopByCenterOrderByCenterInstructorIdDesc(center);
        if (instructor == null) {
            return Long.parseLong("0");
        }
        return instructor.getCenterInstructorId();
    }
}
