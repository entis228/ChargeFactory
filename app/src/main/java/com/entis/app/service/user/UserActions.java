package com.entis.app.service.user;

import com.entis.app.entity.charge.response.ChargeResponse;
import com.entis.app.entity.user.UserStatus;
import com.entis.app.entity.user.request.ChangeUserInfoRequest;
import com.entis.app.entity.user.request.ChangeUserPasswordRequest;
import com.entis.app.entity.user.request.SaveUserRequest;
import com.entis.app.entity.user.request.TopUpAccountRequest;
import com.entis.app.entity.user.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserActions {

    UserResponse create(SaveUserRequest request);

    Optional<UserResponse> findByEmail(String email);

    UserResponse editByEmail(String email, ChangeUserInfoRequest request);

    UserResponse topUp(String email, TopUpAccountRequest request);

    UserResponse changePasswordByEmail(String email, ChangeUserPasswordRequest request);

    UserResponse createAdmin(SaveUserRequest request);

    UserResponse createOwner(SaveUserRequest request);

    Optional<UserResponse> findById(String id);

    UserResponse changeStatusById(String id, UserStatus status);

    UserResponse changePasswordById(String id, String newPassword);

    Page<ChargeResponse> getChargesByEmail(String email, Pageable pageable);

    Page<ChargeResponse> getChargesById(String email, Pageable pageable);

    Page<UserResponse> getAll(Pageable pageable);

    void deleteById(String id);
}
