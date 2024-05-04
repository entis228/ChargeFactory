package com.entis.app.repository;

import com.entis.app.entity.charge.Charge;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChargeRepository extends JpaRepository<Charge, UUID> {

    Page<Charge> findAllByUserId(UUID userId, Pageable pageable);

    Page<Charge> findAllByUserEmail(String email, Pageable pageable);
}
