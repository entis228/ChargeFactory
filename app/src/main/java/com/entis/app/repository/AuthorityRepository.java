package com.entis.app.repository;

import com.entis.app.entity.user.KnownAuthority;
import com.entis.app.entity.user.UserAuthority;

import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<UserAuthority, KnownAuthority> {

    Set<KnownAuthority> ADMIN_AUTHORITIES = EnumSet.of(KnownAuthority.ROLE_USER, KnownAuthority.ROLE_ADMIN);

    Set<KnownAuthority> OWNER_AUTHORITIES = EnumSet.of(KnownAuthority.ROLE_USER, KnownAuthority.ROLE_ADMIN,
                                                       KnownAuthority.ROLE_OWNER);

    Stream<UserAuthority> findAllByIdIn(Set<KnownAuthority> ids);
}
