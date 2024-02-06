package com.olim.customerservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.olim.customerservice.enumeration.Gender;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Customer extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CUSTOMER_ID")
    private long id;
    private String name;
    private Gender gender;
    private String phoneNumber;
    private String address;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    @JoinColumn(name = "INSTRUCTOR_ID")
    private Instructor instructor;

}
