package com.entis.app.entity.station.response;

import com.entis.app.entity.station.Station;
import com.entis.app.entity.station.StationState;

public record StationResponse(
        String id,
        String name,
        StationState state
) {

    public static StationResponse fromStation(Station station) {
        return new StationResponse(
                station.getId().toString(),
                station.getName(),
                station.getState()
        );
    }
}
