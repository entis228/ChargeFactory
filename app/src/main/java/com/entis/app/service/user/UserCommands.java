package com.entis.app.service.user;

import com.entis.app.entity.user.UserStatus;
import com.entis.app.entity.user.request.ChangeUserPasswordRequest;
import com.entis.app.entity.user.request.MergeUserRequest;
import com.entis.app.entity.user.request.OverrideUserPasswordRequest;
import com.entis.app.entity.user.request.SaveUserRequest;
import org.springframework.data.domain.Pageable;

public interface UserActions {

    Page<UserResponse> list(Pageable pageable);

    Optional<UserResponse> findById(long id);

    Optional<UserResponse> findByEmail(String email);

    UserResponse mergeById(long id, MergeUserRequest request);

    UserResponse mergeByEmail(String email, MergeUserRequest request);

    UserResponse create(SaveUserRequest request);

    UserResponse createAdmin(SaveUserRequest request);

    UserResponse changeStatusById(long id, UserStatus status);

    UserResponse changePasswordById(long id, OverrideUserPasswordRequest request);

    UserResponse changePasswordByEmail(String email, ChangeUserPasswordRequest request);

    void deleteById(long id);

    void deleteByEmail(String email);

}
