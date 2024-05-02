package com.entis.app.entity.station.request;

import com.entis.app.entity.station.StationState;
import jakarta.validation.constraints.NotNull;


public record EditStationRequest(
        @NotNull String name,
        @NotNull StationState state) {

}
