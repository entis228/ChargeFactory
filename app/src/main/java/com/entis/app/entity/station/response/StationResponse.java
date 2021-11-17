package com.entis.app.entity.station.response;

import com.entis.app.entity.station.StationState;

public record StationResponse(
        String id,
        String name,
        StationState state
) {
}
