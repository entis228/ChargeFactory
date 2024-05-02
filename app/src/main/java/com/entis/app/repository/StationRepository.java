package com.entis.app.repository;

import com.entis.app.entity.station.Station;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StationRepository extends JpaRepository<Station, UUID> {

}
