package com.olim.customerservice.repository;

import com.olim.customerservice.entity.Center;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CenterRepository extends JpaRepository<Center, UUID> {
    List<Center> findAllByOwner(UUID owner);
}
