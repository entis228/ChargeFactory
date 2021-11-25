package com.entis.app.repository;

import com.entis.app.entity.station.Station;
import com.entis.app.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StationRepository extends JpaRepository<Station, UUID> {

    Optional<Station> findById(String id);
}
