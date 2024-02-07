package com.olim.customerservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Instructor extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long centerInstructorId;
    private UUID userId;
    private String name;
    @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL)
    private List<Customer> customers;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    @JoinColumn(name = "CENTER_ID")
    private Center center;
    @OneToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    @JoinColumn(name = "CUSTOMER_ID")
    private Customer myCustomer;
    @Builder
    public Instructor(
            Long centerInstructorId,
            Center center,
            Customer customer
    ) {
        this.centerInstructorId = centerInstructorId;
        this.center = center;
        this.myCustomer = customer;
        this.name = customer.getName();
    }
}
