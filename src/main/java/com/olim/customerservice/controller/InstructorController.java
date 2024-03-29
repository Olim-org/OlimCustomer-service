package com.olim.customerservice.controller;

import com.olim.customerservice.dto.request.InstructorCreateRequest;
import com.olim.customerservice.dto.request.InstructorModifyRequest;
import com.olim.customerservice.dto.response.InstructorGetListByCenterResponse;
import com.olim.customerservice.service.InstructorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/customer-service/instructor")
@Tag(name = "Instructor", description = "Instructor API 구성")
@Validated
public class InstructorController {
    private final InstructorService instructorService;
    @PostMapping("/create")
    @Operation(description = "강사 등록 엔드포인트")
    @Parameters({
            @Parameter(name = "userId", description = "액세스 토큰 아이디", required = true)
    })
    public ResponseEntity<String> createInstructor(
            @RequestHeader("id") String userId,
            @RequestBody InstructorCreateRequest instructorCreateRequest
    ) {
        return new ResponseEntity<>(this.instructorService.createInstructor(UUID.fromString(userId), instructorCreateRequest), HttpStatus.OK);
    }
    @GetMapping("/list")
    @Operation(description = "센터 강사 목록 가져오기")
    @Parameters({
            @Parameter(name = "centerId", description = "센터 UUID", in = ParameterIn.QUERY, example = "asdf-qr-xcv-daf"),
            @Parameter(name = "userId", description = "액세스 토큰 아이디", required = true, in = ParameterIn.HEADER, example = "asdf-qr-xcv-daf"),
            @Parameter(name = "page", description = "페이지", in = ParameterIn.QUERY, required = false, example = "0"),
            @Parameter(name = "count", description = "페이지 내 아이템 수", in = ParameterIn.QUERY, required = false, example = "20"),
            @Parameter(name = "keyword", description = "검색어", in = ParameterIn.QUERY, example = "김"),
    })
    public ResponseEntity<InstructorGetListByCenterResponse> getInstructorListByCenter(
            @RequestParam(value = "centerId", required = false) String centerId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "count", defaultValue = "20") int count,
            @RequestParam(value = "keyword", defaultValue = "") String keyword,
            @RequestHeader("id") String userId
    ) {
        return new ResponseEntity<>(this.instructorService.getInstructorListByCenter(centerId, UUID.fromString(userId), keyword, page, count), HttpStatus.OK);
    }
    @PutMapping("/{instructorId}")
    @Operation(description = "센터 강사 수정 하기")
    @Parameters({
            @Parameter(name = "userId", description = "액세스 토큰 아이디", required = true, in = ParameterIn.HEADER),
            @Parameter(name = "instructorId", description = "강사 UID", required = true, in = ParameterIn.PATH)
    })
    public ResponseEntity<String> updateInstructor(
            @RequestHeader("id") String userId,
            @PathVariable Long instructorId,
            @RequestBody InstructorModifyRequest instructorModifyRequest
            ) {
        return new ResponseEntity<>(this.instructorService.updateInstructor(UUID.fromString(userId), instructorId, instructorModifyRequest), HttpStatus.OK);
    }
    @DeleteMapping("/{instructorId}")
    @Operation(description = "강사 삭제하기")
    @Parameters({
            @Parameter(name = "userId", description = "액세스 토큰 아이디", required = true, in = ParameterIn.HEADER),
            @Parameter(name = "instructorId", description = "강사 UID", required = true, in = ParameterIn.PATH)
    })
    public ResponseEntity<String> deleteCenter(
            @RequestHeader("id") String userId,
            @PathVariable Long instructorId
    ) {
        return new ResponseEntity<>(this.instructorService.deleteInstructor(UUID.fromString(userId), instructorId), HttpStatus.OK);
    }
}
