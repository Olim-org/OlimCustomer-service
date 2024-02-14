package com.olim.customerservice.service;

import com.olim.customerservice.dto.request.InstructorCreateRequest;
import com.olim.customerservice.dto.request.InstructorModifyRequest;
import com.olim.customerservice.dto.response.InstructorGetListByCenterResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface InstructorService {
    @Transactional
    String createInstructor(UUID userId, InstructorCreateRequest instructorCreateRequest);
    @Transactional
    InstructorGetListByCenterResponse getInstructorListByCenter(String centerId, UUID userId, String keyword, int page, int count);
    @Transactional
    String updateInstructor(UUID userId, Long instructorId, InstructorModifyRequest instructorModifyRequest);
    @Transactional
    String deleteInstructor(UUID userId, Long instructorId);
}
