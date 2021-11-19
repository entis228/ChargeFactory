package com.entis.app.controller;

import com.entis.app.Routes;
import com.entis.app.entity.charge.response.ChargeResponse;
import com.entis.app.entity.user.request.*;
import com.entis.app.entity.user.response.UserResponse;
import com.entis.app.exception.UserOperationExceptions;
import com.entis.app.service.user.UserActions;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping(Routes.USERS)
public class UserController {

    private final UserActions userActions;

    public UserController(UserActions userActions) {
        this.userActions = userActions;
    }

//    USER SECTION
    @PostMapping("/new")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@RequestBody @Valid SaveUserRequest request) {
        return userActions.create(request);
    }

    @GetMapping("/current")
    public UserResponse getCurrentUser(@AuthenticationPrincipal String email) {
        return userActions.findByEmail(email).orElseThrow(() -> UserOperationExceptions.userWithEmailNotFound(email));
    }

    @PatchMapping("/current")
    public UserResponse editCurrentUser(@AuthenticationPrincipal String email,
                                         @RequestBody @Valid ChangeUserInfoRequest request) {
        return userActions.editByEmail(email, request);
    }

    @PostMapping("/current/add")
    public UserResponse addToBalance(@AuthenticationPrincipal String email,
                                     @RequestBody @Valid TopUpAccountRequest request){
        return userActions.topUp(email,request);
    }

    @PatchMapping("/current/password")
    public UserResponse changeCurrentUserPassword(@AuthenticationPrincipal String email,
                                                  @RequestBody @Valid ChangeUserPasswordRequest request) {
        return userActions.changePasswordByEmail(email, request);
    }

    @GetMapping("/current/charges")
    @PageableAsQueryParam
    public Page<ChargeResponse> getUserCharges(@AuthenticationPrincipal String email, Pageable pageable) {
        return userActions.getChargesByEmail(email,pageable);
    }


    //    ADMIN SECTION
    @GetMapping("/{id}")
    public UserResponse findUserById(@PathVariable String id){
        return userActions.findById(id).orElseThrow(() -> UserOperationExceptions.userWithIdNotFound(id));
    }

    @GetMapping("/{id}/email")
    public UserResponse findUserByEmail(@PathVariable @Email String email){
        return userActions.findByEmail(email).orElseThrow(() -> UserOperationExceptions.userWithEmailNotFound(email));
    }

    @PatchMapping("/{id}/status")
    public UserResponse changeUserStatusById(@PathVariable String id,
                                             @RequestBody @Valid ChangeUserStatusRequest request) {
        return userActions.changeStatusById(id, request.status());
    }

    @PatchMapping("/{id}/password")
    public UserResponse changeUserPasswordById(@PathVariable String id,
                                             @RequestBody @Valid SetUserPasswordRequest request) {
        return userActions.changePasswordById(id, request.newPassword());
    }

    @GetMapping("/{id}/charges")
    @PageableAsQueryParam
    public Page<ChargeResponse> getChargesByUserId(@PathVariable String id, Pageable pageable) {
        return userActions.getChargesById(id,pageable);
    }

    @GetMapping
    @PageableAsQueryParam
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        return userActions.getAll(pageable);
    }

    //    OWNER SECTION
    @PostMapping("/admins")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse registerAdmin(@RequestBody @Valid SaveUserRequest request) {
        return userActions.createAdmin(request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserById(@NotNull @PathVariable String id) {
        userActions.deleteById(id);
    }


    @PostMapping("/owners")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse registerOwner(@RequestBody @Valid SaveUserRequest request) {
        return userActions.createOwner(request);
    }
}
