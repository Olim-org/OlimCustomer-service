package com.olim.customerservice.service.impl;

import com.olim.customerservice.dto.request.InstructorCreateRequest;
import com.olim.customerservice.dto.response.InstructorGetListByCenterResponse;
import com.olim.customerservice.entity.Center;
import com.olim.customerservice.entity.CenterAdmin;
import com.olim.customerservice.entity.Instructor;
import com.olim.customerservice.exception.customexception.DataNotFoundException;
import com.olim.customerservice.repository.CenterAdminRepository;
import com.olim.customerservice.repository.CenterRepository;
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
import java.util.zip.DataFormatException;

@Service
@RequiredArgsConstructor
@Slf4j
public class InstructorServiceImpl implements InstructorService {
    private final InstructorRepository instructorRepository;
    private final CenterRepository centerRepository;
    private final CenterAdminRepository centerAdminRepository;
    @Transactional
    @Override
    public String createInstructor(UUID userId, InstructorCreateRequest instructorCreateRequest) {
        Optional<Center> center = this.centerRepository.findById(instructorCreateRequest.centerId());
        if (!center.isPresent()) {
            throw new DataNotFoundException("해당 센터를 찾을 수 없습니다.");
        }
        Optional<CenterAdmin> centerAdmin = centerAdminRepository.findByCenterAndUserId(center.get(), userId);
        if (!centerAdmin.isPresent()) {
            throw new DataNotFoundException("센터 관리자가 아닙니다.");
        }
        Instructor instructor = Instructor.builder()
                .userId(instructorCreateRequest.userId())
                .name(instructorCreateRequest.name())
                .center(center.get())
                .build();
        this.instructorRepository.save(instructor);
        return "성공적으로 " + instructorCreateRequest.name() + "님이 강사로 등록 되었습니다.";
    }
    @Transactional
    @Override
    public InstructorGetListByCenterResponse getInstructorListByCenter(UUID centerId, UUID userId, String keyword, int page, int count) {
        Optional<Center> center = this.centerRepository.findById(centerId);
        if (!center.isPresent()) {
            throw new DataNotFoundException("해당 센터는 존재하지 않습니다.");
        }
        Optional<CenterAdmin> centerAdmin = this.centerAdminRepository.findByCenterAndUserId(center.get(), userId);
        if (!centerAdmin.isPresent()) {
            throw new DataNotFoundException("센터 관리자가 아닙니다.");
        }
        Sort sort = Sort.by("name").ascending();

        Pageable pageable = PageRequest.of(page, count, sort);

        Page<Instructor> instructors = this.instructorRepository.findAllByCenterAndNameStartingWith(center.get(), keyword, pageable);
        InstructorGetListByCenterResponse instructorGetListByCenterResponse =
                InstructorGetListByCenterResponse.makeDto(instructors.getContent());
        return instructorGetListByCenterResponse;
    }
}
