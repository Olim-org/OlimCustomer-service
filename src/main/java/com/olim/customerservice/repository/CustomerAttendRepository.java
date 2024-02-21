package com.olim.customerservice.repository;

import com.olim.customerservice.entity.CustomerAttend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CustomerAttendRepository extends JpaRepository<CustomerAttend, UUID> {
}
