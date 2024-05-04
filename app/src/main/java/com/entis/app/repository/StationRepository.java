package com.entis.app.repository;

import com.entis.app.entity.station.Station;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StationRepository extends JpaRepository<Station, UUID> {

}
