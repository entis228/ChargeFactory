package com.entis.app.repository;

import com.entis.app.entity.auth.RefreshToken;
import com.entis.app.entity.user.UserStatus;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    @Query("select rt from RefreshToken rt inner join fetch rt.user u " +
           "where rt.value = :value and rt.expireAt > :when and u.status = :status")
    Optional<RefreshToken> findIfValid(UUID value, OffsetDateTime when, UserStatus status);

    @Query("delete from RefreshToken rt where rt = :head or rt.next = :head")
    @Modifying
    void deleteChain(RefreshToken head);

    @Query("update RefreshToken rt set rt.next = :newHead where rt = :oldHead or rt.next = :oldHead")
    @Modifying
    void updateChain(RefreshToken oldHead, RefreshToken newHead);

}
