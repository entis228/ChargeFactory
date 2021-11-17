package com.entis.app.controller;

import com.entis.app.Routes;
import com.entis.app.entity.station.request.EditStationRequest;
import com.entis.app.entity.station.response.StationResponse;
import com.entis.app.exception.StationOperationException;
import com.entis.app.service.station.StationActions;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping(Routes.STATIONS)
public class StationController {

    private final StationActions stationActions;

    public StationController(StationActions stationActions) {
        this.stationActions = stationActions;
    }

    @GetMapping
    @PageableAsQueryParam
    public Page<StationResponse> getAll(Pageable pageable){
        return stationActions.getAll(pageable);
    }

    @GetMapping("/{id}")
    public StationResponse getById(@PathVariable String id){
        return stationActions.findById(id).orElseThrow(() -> StationOperationException.stationWithIdNotFound(id));
    }

    @PostMapping("/new")
    @ResponseStatus(HttpStatus.CREATED)
    public StationResponse create(@NotNull @RequestBody String name){
        return stationActions.create(name);
    }

    @PatchMapping("/{id}")
    public StationResponse editById(@NotNull @PathVariable String id,
                                    @RequestBody @Valid EditStationRequest request) {
        return stationActions.editById(id,request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserById(@NotNull @PathVariable String id) {
        stationActions.deleteById(id);
    }
}