package com.entis.app.entity.user;

import com.entis.app.entity.charge.Charge;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.*;

@Entity
@Table(name = "users")
@Getter
@Setter

public class User {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;

    @NaturalId(mutable = true)
    @Column(nullable = false, unique = true)
    private String email;

    private String surname;
    private String phone;

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private UserStatus status = UserStatus.ACTIVE;

    @ManyToMany
    @JoinTable(name = "user_authorities",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "id")
    )
    @MapKeyEnumerated(EnumType.ORDINAL)
    @MapKey(name = "id")
    private Map<KnownAuthority, UserAuthority> authorities = new EnumMap<>(KnownAuthority.class);

    @Column(nullable = false)
    private BigDecimal balance;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @OneToMany(mappedBy = "user" ,cascade = CascadeType.ALL)
    private List<Charge>charges=new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return email.equals(user.email);
    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }

}
