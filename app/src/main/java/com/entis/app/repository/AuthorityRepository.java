package com.entis.app.repository;

import com.entis.app.entity.user.KnownAuthority;
import com.entis.app.entity.user.UserAuthority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Stream;

public interface AuthorityRepository extends JpaRepository<UserAuthority, KnownAuthority> {

    Set<KnownAuthority> ADMIN_AUTHORITIES = EnumSet.of(KnownAuthority.ROLE_USER, KnownAuthority.ROLE_ADMIN, KnownAuthority.ROLE_OWNER);

    Stream<UserAuthority> findAllByIdIn(Set<KnownAuthority> ids);
}
