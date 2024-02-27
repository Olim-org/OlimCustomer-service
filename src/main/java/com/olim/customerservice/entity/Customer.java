package com.olim.customerservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.olim.customerservice.dto.request.CustomerPutProfileRequest;
import com.olim.customerservice.enumeration.CustomerRole;
import com.olim.customerservice.enumeration.CustomerStatus;
import com.olim.customerservice.enumeration.Gender;
import com.olim.customerservice.enumeration.VisitRoute;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Customer extends BaseEntity {
    // 올림 uid
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CUSTOMER_ID")
    private Long id;
    // 센터 uid
    private Long centerCustomerId;
    private UUID userId;
    // 고객 개인 정보
    private String name;
    @Enumerated(value = EnumType.STRING)
    private Gender gender;
    private LocalDate birthDate;
    private String phoneNumber;
    @Column(length = 1000)
    private String address;
    private UUID owner;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    @JoinColumn(name = "INSTRUCTOR_ID")
    private Instructor instructor;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    @JoinColumn(name = "CENTER_ID")
    private Center center;
    private CustomerRole role;
    // 고객 추가 기입 정보
    @Enumerated(value = EnumType.STRING)
    private VisitRoute visitRoute;
    @Column(length = 1000)
    private String healthExp;
    @Column(length = 1000)
    private String purpose;
    @Column(length = 1000)
    private String diseases;
    @Column(length = 1000)
    private String pregnant;
    @Column(length = 1000)
    private String lifeHabit;
    @Column(length = 1000)
    private String desiredTimeSlot;
    private Boolean kakaoTalkAlert;
    @Enumerated(value = EnumType.STRING)
    private CustomerStatus status;
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<CustomerAttend> customerAttends;
    private Integer blackAttendCounts;
    @Column(length = 1000)
    private String othersReason;
    private LocalDateTime createdAt;
    @Builder
    public Customer(
            Long centerCustomerId,
            String name,
            Gender gender,
            LocalDate birthDate,
            String phoneNumber,
            String address,
            Center center,
            Instructor instructor,
            UUID owner
    ) {
        this.centerCustomerId = centerCustomerId;
        this.name = name;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.address = address;
        this.role = CustomerRole.CUSTOMER_USER;
        this.center = center;
        this.visitRoute = VisitRoute.OTHERS;
        this.instructor = instructor;
        this.owner = owner;
        this.status = CustomerStatus.ACTIVE;
        this.customerAttends = new ArrayList<>();
        this.blackAttendCounts = 0;
        this.createdAt = LocalDateTime.now();
    }
    public void updateRole(CustomerRole role) {
        this.role = role;
    }
    public void updateProfile(CustomerPutProfileRequest customerPutProfileRequest, Instructor instructor) {
        this.name = customerPutProfileRequest.name();
        this.gender = customerPutProfileRequest.gender();
        this.birthDate = LocalDate.parse(customerPutProfileRequest.birthDate(), DateTimeFormatter.ISO_DATE);
        this.address = customerPutProfileRequest.address();
        this.visitRoute = customerPutProfileRequest.visitRoute();
        if (customerPutProfileRequest.visitRoute().getKey().equals("OTHERS")) {
            this.othersReason = customerPutProfileRequest.othersReason();
        }
        this.healthExp = customerPutProfileRequest.healthExp();
        this.purpose = customerPutProfileRequest.purpose();
        this.diseases = customerPutProfileRequest.diseases();
        this.pregnant = customerPutProfileRequest.pregnant();
        this.lifeHabit = customerPutProfileRequest.lifeHabit();
        this.desiredTimeSlot = customerPutProfileRequest.desiredTimeSlot();
        this.kakaoTalkAlert = customerPutProfileRequest.kakaoTalkAlert();
        this.instructor = instructor;
        this.status = customerPutProfileRequest.status();
    }
    public void deletedByCenter() {
        this.status = CustomerStatus.CENTER_DELETED;
    }
    public void deleteCustomer() {
        this.status = CustomerStatus.DELETE;
    }
    public void addBlackAttendCounts() {
        this.blackAttendCounts++;
    }
}
