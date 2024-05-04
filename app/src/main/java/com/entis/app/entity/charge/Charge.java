package com.entis.app.entity.charge;

import com.entis.app.entity.station.Station;
import com.entis.app.entity.user.User;

import java.time.OffsetDateTime;
import java.util.UUID;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "charges")
@Getter
@Setter
@EqualsAndHashCode
public class Charge {

    @Id
    @GeneratedValue
    UUID id;

    @Column(nullable = false)
    private OffsetDateTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_id")
    @Access(AccessType.PROPERTY)
    private Station station;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @Access(AccessType.PROPERTY)
    private User user;

    @Column(nullable = false, name = "consumed")
    private Double consumedEnergy;

    @Column(nullable = false)
    private Double withdraw;
}
