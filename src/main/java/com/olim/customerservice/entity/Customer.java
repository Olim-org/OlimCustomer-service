package com.olim.customerservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.olim.customerservice.enumeration.CustomerRole;
import com.olim.customerservice.enumeration.Gender;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Customer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CUSTOMER_ID")
    private Long id;
    private Long centerCustomerId;
    private UUID userId;
    private String name;
    @Enumerated(value = EnumType.STRING)
    private Gender gender;
    private LocalDate birthDate;
    private String phoneNumber;
    private String address;
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

    @OneToOne(mappedBy = "myCustomer", cascade = CascadeType.ALL)
    private Instructor myInstructor;
    private CustomerRole role;
    // 고객 추가 기입 정보

    @Builder
    public Customer(
            Long centerCustomerId,
            String name,
            Gender gender,
            LocalDate birthDate,
            String phoneNumber,
            String address,
            Center center,
            Instructor instructor
    ) {
        this.centerCustomerId = centerCustomerId;
        this.name = name;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.address = address;
        this.role = CustomerRole.CUSTOMER_USER;
        this.center = center;
        this.instructor = instructor;
    }
    public void updateRole(CustomerRole role) {
        this.role = role;
    }
}
