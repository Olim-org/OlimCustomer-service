package com.olim.customerservice.repository;

import com.olim.customerservice.entity.CenterAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface CenterAdminRepository extends JpaRepository<CenterAdmin, UUID> {
    Optional<CenterAdmin> findByUserId(UUID userId);
}
