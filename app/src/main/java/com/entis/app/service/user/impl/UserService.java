package com.entis.app.service.user.impl;

import com.entis.app.entity.charge.response.ChargeResponse;
import com.entis.app.entity.user.UserStatus;
import com.entis.app.entity.user.request.ChangeUserInfoRequest;
import com.entis.app.entity.user.request.ChangeUserPasswordRequest;
import com.entis.app.entity.user.request.SaveUserRequest;
import com.entis.app.entity.user.request.TopUpAccountRequest;
import com.entis.app.entity.user.response.UserResponse;
import com.entis.app.service.user.UserActions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserActions, UserDetailsService {

    @Override
    public UserResponse create(SaveUserRequest request) {
        return null;
    }

    @Override
    public Optional<UserResponse> findByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public UserResponse editByEmail(String email, ChangeUserInfoRequest request) {
        return null;
    }

    @Override
    public UserResponse topUp(String email, TopUpAccountRequest request) {
        return null;
    }

    @Override
    public UserResponse changePasswordByEmail(String email, ChangeUserPasswordRequest request) {
        return null;
    }

    @Override
    public UserResponse createAdmin(SaveUserRequest request) {
        return null;
    }

    @Override
    public UserResponse createOwner(SaveUserRequest request) {
        return null;
    }

    @Override
    public Optional<UserResponse> findById(String id) {
        return Optional.empty();
    }

    @Override
    public UserResponse changeStatusById(String id, UserStatus status) {
        return null;
    }

    @Override
    public UserResponse changePasswordById(String id, String newPassword) {
        return null;
    }

    @Override
    public Page<ChargeResponse> getChargesByEmail(String email, Pageable pageable) {
        return null;
    }

    @Override
    public Page<ChargeResponse> getChargesById(String email, Pageable pageable) {
        return null;
    }

    @Override
    public Page<UserResponse> getAll(Pageable pageable) {
        return null;
    }

    @Override
    public void deleteById(String id) {

    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return null;
    }

    @Transactional
    public void mergeAdmins(List<SaveUserRequest> requests){

    }

}
