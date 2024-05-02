package com.entis.app.controller;

import com.entis.app.Routes;
import com.entis.app.entity.charge.response.ChargeResponse;
import com.entis.app.entity.user.request.ChangeUserInfoRequest;
import com.entis.app.entity.user.request.ChangeUserPasswordRequest;
import com.entis.app.entity.user.request.ChangeUserStatusRequest;
import com.entis.app.entity.user.request.SaveUserRequest;
import com.entis.app.entity.user.request.SetUserPasswordRequest;
import com.entis.app.entity.user.request.TopUpAccountRequest;
import com.entis.app.entity.user.response.UserResponse;
import com.entis.app.exception.UserOperationExceptions;
import com.entis.app.service.user.UserActions;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Routes.USERS)
public class UserController {

    private final UserActions userActions;

    public UserController(UserActions userActions) {
        this.userActions = userActions;
    }

    //    USER SECTION
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@RequestBody @Valid SaveUserRequest request) {
        return userActions.create(request);
    }

    @GetMapping(value = "/current", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserResponse getCurrentUser(@AuthenticationPrincipal String email) {
        return userActions.findByEmail(email)
            .orElseThrow(() -> UserOperationExceptions.userWithEmailNotFound(email));
    }

    @PatchMapping("/current")
    public UserResponse editCurrentUser(@AuthenticationPrincipal String email,
                                        @RequestBody @Valid ChangeUserInfoRequest request) {
        return userActions.editByEmail(email, request);
    }

    @PostMapping("/current/add")
    public UserResponse addToBalance(@AuthenticationPrincipal String email,
                                     @RequestBody @Valid TopUpAccountRequest request) {
        return userActions.topUp(email, request);
    }

    @PatchMapping("/current/password")
    public UserResponse changeCurrentUserPassword(@AuthenticationPrincipal String email,
                                                  @RequestBody @Valid
                                                  ChangeUserPasswordRequest request) {
        return userActions.changePasswordByEmail(email, request);
    }

    @GetMapping("/current/charges")
    @PageableAsQueryParam
    public Page<ChargeResponse> getUserCharges(@AuthenticationPrincipal String email,
                                               @Parameter(hidden = true) Pageable pageable) {
        return userActions.getChargesByEmail(email, pageable);
    }

    //    ADMIN SECTION
    @GetMapping("/id/{id}")
    public UserResponse findUserById(@PathVariable @Size(max = 36) String id) {
        return userActions.findById(id)
            .orElseThrow(() -> UserOperationExceptions.userWithIdNotFound(id));
    }

    @GetMapping("/email/{email}")
    public UserResponse findUserByEmail(@PathVariable @Email String email) {
        return userActions.findByEmail(email)
            .orElseThrow(() -> UserOperationExceptions.userWithEmailNotFound(email));
    }

    @PatchMapping("/id/{id}/status")
    public UserResponse changeUserStatusById(@PathVariable @Size(max = 36) String id,
                                             @RequestBody @Valid ChangeUserStatusRequest request) {
        return userActions.changeStatusById(id, request.status());
    }

    @PatchMapping("/id/{id}/password")
    public UserResponse changeUserPasswordById(@PathVariable @Size(max = 36) String id,
                                               @RequestBody @Valid SetUserPasswordRequest request) {
        return userActions.changePasswordById(id, request.newPassword());
    }

    @GetMapping("/id/{id}/charges")
    @PageableAsQueryParam
    public Page<ChargeResponse> getChargesByUserId(@PathVariable @Size(max = 36) String id,
                                                   @Parameter(hidden = true) Pageable pageable) {
        return userActions.getChargesById(id, pageable);
    }

    @GetMapping
    @PageableAsQueryParam
    public Page<UserResponse> getAllUsers(@Parameter(hidden = true) Pageable pageable) {
        return userActions.getAll(pageable);
    }

    //    OWNER SECTION
    @PostMapping("/admins")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse registerAdmin(@RequestBody @Valid SaveUserRequest request) {
        return userActions.createAdmin(request);
    }

    @DeleteMapping("/id/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserById(@NotNull @Size(max = 36) @PathVariable String id) {
        userActions.deleteById(id);
    }

    @PostMapping("/owners")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse registerOwner(@RequestBody @Valid SaveUserRequest request) {
        return userActions.createOwner(request);
    }
}
