package com.entis.app.service.station.impl;

import com.entis.app.entity.station.request.EditStationRequest;
import com.entis.app.entity.station.response.StationResponse;
import com.entis.app.service.station.StationActions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StationService implements StationActions {

    @Override
    public Page<StationResponse> getAll(Pageable pageable) {
        return null;
    }

    @Override
    public Optional<StationResponse> findById(String id) {
        return Optional.empty();
    }

    @Override
    public StationResponse create(String name) {
        return null;
    }

    @Override
    public StationResponse editById(String id, EditStationRequest request) {
        return null;
    }

    @Override
    public void deleteById(String id) {

    }
}
