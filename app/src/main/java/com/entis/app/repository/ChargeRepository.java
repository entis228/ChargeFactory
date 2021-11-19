package com.entis.app.repository;

import com.entis.app.entity.charge.Charge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ChargeRepository extends JpaRepository<Charge, UUID> {

    @Query("select ch from Charge ch where ch.user =: userId")
    Page<Charge> findAllByUserId(UUID userId, Pageable pageable);

    @Query("select ch from Charge ch join ch.user u where u.email =: email")
    Page<Charge>findAllByUserEmail(String email, Pageable pageable);
}
