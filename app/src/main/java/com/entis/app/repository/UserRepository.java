package com.entis.app.repository;

import com.entis.app.entity.user.User;
import com.entis.app.entity.user.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("update User u set u.status = :status where u.email = :email")
    @Modifying
    void changeStatusByEmail(String email, UserStatus status);

}
