package com.olim.customerservice.entity;

import com.olim.customerservice.dto.request.CenterModifyRequest;
import com.olim.customerservice.enumeration.CenterStatus;
import com.olim.customerservice.service.CenterService;
import com.olim.customerservice.service.CustomerService;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Center extends BaseEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "CENTER_ID", columnDefinition = "BINARY(16)")
    private UUID id;
    private String name;
    private String description;
    private String phoneNumber;
    private String address;
    private String imageUrl;
    @OneToMany(mappedBy = "center", cascade = CascadeType.ALL)
    private Set<Customer> customers;
    @OneToMany(mappedBy = "center", cascade = CascadeType.ALL)
    private Set<Instructor> instructors;
    private UUID owner;
    private CenterStatus status;
    @Builder
    public Center(
        String name,
        UUID owner
    ) {
        this.name = name;
        this.customers = new HashSet<>();
        this.instructors = new HashSet<>();
        this.owner = owner;
        this.status = CenterStatus.WAIT;
    }
    public void deleteCenter() {
        this.status = CenterStatus.DELETE;
    }
    public void updateCenter(
            CenterModifyRequest centerModifyRequest
    ) {
        this.name = centerModifyRequest.name();
        this.description = centerModifyRequest.description();
        this.phoneNumber = centerModifyRequest.phoneNumber();
        this.address = centerModifyRequest.address();
        this.imageUrl = centerModifyRequest.imageUrl();
    }
}
