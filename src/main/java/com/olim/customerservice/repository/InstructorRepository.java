package com.olim.customerservice.repository;

import com.olim.customerservice.entity.Center;
import com.olim.customerservice.entity.Customer;
import com.olim.customerservice.entity.Instructor;
import com.olim.customerservice.enumeration.InstructorStatus;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, Long> {
    Page<Instructor> findAllByCenterAndStatusNotInAndNameContaining(Center center, List<InstructorStatus> status, String name, Pageable pageable);
    List<Instructor> findAllByCenter(Center center);
    Instructor findTopByCenterOrderByCenterInstructorIdDesc(Center center);
    Page<Instructor> findAllByOwnerAndStatusNotInAndNameContaining(UUID owner, List<InstructorStatus> status, String name, Pageable pageable);
}
