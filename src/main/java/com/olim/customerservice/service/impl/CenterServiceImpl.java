package com.olim.customerservice.service.impl;

import com.olim.customerservice.dto.request.CenterCreateRequest;
import com.olim.customerservice.dto.response.CenterCreateResponse;
import com.olim.customerservice.dto.response.CenterGetResponse;
import com.olim.customerservice.entity.Center;
import com.olim.customerservice.entity.CenterAdmin;
import com.olim.customerservice.exception.customexception.DataNotFoundException;
import com.olim.customerservice.exception.customexception.DuplicateException;
import com.olim.customerservice.repository.CenterAdminRepository;
import com.olim.customerservice.repository.CenterRepository;
import com.olim.customerservice.service.CenterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CenterServiceImpl implements CenterService {
    private final CenterRepository centerRepository;
    private final CenterAdminRepository centerAdminRepository;
    @Transactional
    @Override
    public CenterCreateResponse createCenter(CenterCreateRequest centerCreateRequest, UUID userId) {
        Optional<Center> center = this.centerRepository.findByOwner(userId);
        if (center.isPresent()) {
            throw new DuplicateException("센터는 1 계정 당 1개만 만들 수 있습니다.");
        }
        Center newCenter = Center.builder()
                .name(centerCreateRequest.name())
                .owner(userId)
                .build();
        Center savedCenter = this.centerRepository.save(newCenter);
        CenterAdmin centerAdmin = CenterAdmin.builder()
                .center(savedCenter)
                .userId(userId)
                .build();
        this.centerAdminRepository.save(centerAdmin);
        CenterCreateResponse centerCreateResponse = CenterCreateResponse.makeDto(savedCenter);
        return centerCreateResponse;
    }

    @Override
    public CenterGetResponse getMyCenter(UUID userId) {
        Optional<Center> center = this.centerRepository.findByOwner(userId);
        if (!center.isPresent()) {
            throw new DataNotFoundException("해당 유저가 소유 중인 센터가 존재하지 않습니다");
        }
        CenterGetResponse centerGetResponse = CenterGetResponse.makeDto(center.get());
        return centerGetResponse;
    }
}
