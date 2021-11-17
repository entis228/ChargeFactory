package com.entis.app.service.station;

import com.entis.app.entity.station.request.EditStationRequest;
import com.entis.app.entity.station.response.StationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface StationActions {

    Page<StationResponse>getAll(Pageable pageable);

    Optional<StationResponse> findById(String id);

    StationResponse create(String name);

    StationResponse editById(String id, EditStationRequest request);

    void deleteById(String id);
}
