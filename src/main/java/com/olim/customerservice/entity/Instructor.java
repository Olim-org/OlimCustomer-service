package com.olim.customerservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.olim.customerservice.enumeration.Gender;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
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
    private String address;
    private UUID owner;
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
    }
}
