package com.olim.customerservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CustomerAttend extends BaseEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "CUSTOMER_ATTEND_ID", columnDefinition = "BINARY(16)")
    private UUID id;
    private UUID attendId;
    private Boolean isBlackConsumer;
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonIgnore
    @JoinColumn(name = "CUSTOMER_ID")
    private Customer customer;
    @Builder
    public CustomerAttend(
            UUID attendId,
            Customer customer,
            Boolean isBlackConsumer
    ) {
        this.attendId = attendId;
        this.customer = customer;
        this.isBlackConsumer = isBlackConsumer;
    }
}
