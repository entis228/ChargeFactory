package com.entis.app.entity.charge;

import com.entis.app.entity.station.Station;
import com.entis.app.entity.user.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

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
    private double consumedEnergy;

    @Column(nullable = false)
    private double withdraw;
}
