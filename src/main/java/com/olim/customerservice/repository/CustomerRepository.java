package com.olim.customerservice.repository;

import com.olim.customerservice.entity.Center;
import com.olim.customerservice.entity.Customer;
import com.olim.customerservice.entity.Instructor;
import com.olim.customerservice.enumeration.CustomerRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findAllByCenter(Center center);
    Optional<Customer> findByCenterAndUserId(Center center, UUID userId);
    Customer findTopByCenterOrderByCenterCustomerIdDesc(Center center);
    Optional<Customer> findByCenterAndId(Center center, Long id);
    Page<Customer> findAllByCenterAndRoleAndNameContaining(Center center, CustomerRole role, String name, Pageable pageable);
    Page<Customer> findAllByOwnerAndRoleAndNameContaining(UUID owner, CustomerRole role, String name, Pageable pageable);
}
