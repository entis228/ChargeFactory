package com.entis.app.entity.auth;

import com.entis.app.entity.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {

    @Id
    @GeneratedValue
    private UUID value;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "issued_at", nullable = false)
    private OffsetDateTime issuedAt;

    @Column(name = "expire_at", nullable = false)
    private OffsetDateTime expireAt;

    @SuppressWarnings("FieldMayBeFinal")
    @OneToMany(mappedBy = "next", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RefreshToken> previous = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "next")
    @Access(AccessType.PROPERTY)
    private RefreshToken next;
}
