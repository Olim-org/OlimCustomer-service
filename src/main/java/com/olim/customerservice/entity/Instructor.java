package com.olim.customerservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.olim.customerservice.dto.request.InstructorModifyRequest;
import com.olim.customerservice.enumeration.Gender;
import com.olim.customerservice.enumeration.InstructorStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Instructor extends BaseEntity {
    // 올림 uid
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // 센터 uid
    private Long centerInstructorId;
    // 강사 개인 정보
    private UUID userId;
    private String name;
    @Enumerated(value = EnumType.STRING)
    private Gender gender;
    private LocalDate birthDate;
    private String phoneNumber;
    @Column(length = 1000)
    private String address;
    private UUID owner;
    @Enumerated(value = EnumType.STRING)
    private InstructorStatus status;
    @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL)
    private List<Customer> customers;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    @JoinColumn(name = "CENTER_ID")
    private Center center;
    @Builder
    public Instructor(
            Long centerInstructorId,
            String name,
            Gender gender,
            LocalDate birthDate,
            String phoneNumber,
            String address,
            Center center,
            UUID owner
    ) {
        this.centerInstructorId = centerInstructorId;
        this.name = name;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.address = address;
        this.center = center;
        this.owner = owner;
        this.status = InstructorStatus.INACTIVE;
        this.customers = new ArrayList<>();
    }
    public void updateInstructor(
            InstructorModifyRequest instructorModifyRequest
    ) {
        this.name = instructorModifyRequest.name();
        this.gender = instructorModifyRequest.gender();
        this.status = instructorModifyRequest.status() != InstructorStatus.DELETE ? instructorModifyRequest.status() : InstructorStatus.INACTIVE;
        this.birthDate = LocalDate.parse(instructorModifyRequest.birthDate(), DateTimeFormatter.ISO_DATE);
        this.address = instructorModifyRequest.address();
    }
    public void deleteInstructor() {
        this.status = InstructorStatus.DELETE;
    }
    public void deletedByCenter() {
        this.status = InstructorStatus.CENTER_DELETED;
    }

}
