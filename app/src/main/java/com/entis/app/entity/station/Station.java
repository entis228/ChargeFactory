package com.entis.app.entity.station;

import com.entis.app.entity.charge.Charge;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "stations")
public class Station {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private StationState state=StationState.WAITING_FOR_PLUG;

    @Column(nullable = false)
    @OneToMany(mappedBy = "station", cascade = CascadeType.ALL)
    private List<Charge>charges=new ArrayList<>();
}
