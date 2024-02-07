package com.olim.customerservice.repository;

import com.olim.customerservice.entity.Center;
import com.olim.customerservice.entity.Instructor;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, Long> {
    Page<Instructor> findAllByCenterAndNameStartingWith(Center center, String name, Pageable pageable);
}
