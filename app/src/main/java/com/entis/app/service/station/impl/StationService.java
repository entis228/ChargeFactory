package com.entis.app.service.station.impl;

import com.entis.app.entity.station.Station;
import com.entis.app.entity.station.StationState;
import com.entis.app.entity.station.request.EditStationRequest;
import com.entis.app.entity.station.response.StationResponse;
import com.entis.app.repository.StationRepository;
import com.entis.app.service.station.StationActions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StationService implements StationActions {

    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Override
    public Page<StationResponse> getAll(Pageable pageable) {
        Page<Station>stations=stationRepository.findAll(pageable);
        List<StationResponse>result=new ArrayList<>();
        stations.forEach(x->result.add(StationResponse.fromStation(x)));
        return new PageImpl<>(result);
    }

    @Override
    public Optional<StationResponse> findById(String id) {
        return stationRepository.findById(UUID.fromString(id)).map(StationResponse::fromStation);
    }

    @Override
    public StationResponse create(String name) {
        Station station=new Station();
        station.setName(name);
        station.setState(StationState.WAITING_FOR_PLUG);
        station.setCharges(new ArrayList<>());
        stationRepository.save(station);
        return StationResponse.fromStation(station);
    }

    @Override
    public StationResponse editById(String id, EditStationRequest request) {
        Station dbStation=stationRepository.getById(UUID.fromString(id));
        dbStation.setName(request.name());
        dbStation.setState(request.state());
        stationRepository.save(dbStation);
        return StationResponse.fromStation(dbStation);
    }

    @Override
    public void deleteById(String id) {
        stationRepository.deleteById(UUID.fromString(id));
    }
}
