package com.olim.customerservice.entity;

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
    @OneToMany(mappedBy = "center", cascade = CascadeType.ALL)
    private List<CenterAdmin> centerAdmins;
    @OneToMany(mappedBy = "center", cascade = CascadeType.ALL)
    private Set<Customer> customers;
    private UUID owner;
    @Builder
    public Center(
        String name,
        UUID owner
    ) {
        this.name = name;
        this.centerAdmins = new ArrayList<>();
        this.customers = new HashSet<>();
        this.owner = owner;
    }
}
