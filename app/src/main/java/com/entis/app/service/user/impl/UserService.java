package com.entis.app.service.user.impl;

import com.entis.app.entity.auth.AuthUserDetails;
import com.entis.app.entity.charge.response.ChargeResponse;
import com.entis.app.entity.user.KnownAuthority;
import com.entis.app.entity.user.User;
import com.entis.app.entity.user.UserAuthority;
import com.entis.app.entity.user.UserStatus;
import com.entis.app.entity.user.request.ChangeUserInfoRequest;
import com.entis.app.entity.user.request.ChangeUserPasswordRequest;
import com.entis.app.entity.user.request.SaveUserRequest;
import com.entis.app.entity.user.request.TopUpAccountRequest;
import com.entis.app.entity.user.response.UserResponse;
import com.entis.app.exception.UserOperationExceptions;
import com.entis.app.repository.AuthorityRepository;
import com.entis.app.repository.ChargeRepository;
import com.entis.app.repository.UserRepository;
import com.entis.app.service.user.UserActions;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import io.swagger.v3.oas.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService implements UserActions, UserDetailsService {

    private final UserRepository userRepository;

    private final AuthorityRepository authorityRepository;

    private final ChargeRepository chargeRepository;

    private final PasswordEncoder passwordEncoder;

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository, AuthorityRepository authorityRepository,
                       ChargeRepository chargeRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.chargeRepository = chargeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserResponse> findById(String id) {
        return userRepository.findById(UUID.fromString(id)).map(UserResponse::fromUser);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserResponse> findByEmail(String email) {
        return userRepository.findByEmail(email).map(UserResponse::fromUser);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ChargeResponse> getChargesByEmail(String email,
                                                  @Parameter(hidden = true)
                                                  Pageable pageable) {
        List<ChargeResponse> result = chargeRepository.findAllByUserEmail(email, pageable)
            .stream()
            .map(ChargeResponse::fromCharge)
            .toList();
        return new PageImpl<>(result);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ChargeResponse> getChargesById(String id,
                                               @Parameter(hidden = true)
                                               Pageable pageable) {
        List<ChargeResponse> result = chargeRepository.findAllByUserId(UUID.fromString(id), pageable)
            .stream()
            .map(ChargeResponse::fromCharge)
            .toList();
        return new PageImpl<>(result);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponse> getAll(
        @Parameter(hidden = true)
        Pageable pageable) {
        return userRepository.findAll(pageable).map(UserResponse::fromUser);
    }

    @Override
    @Transactional
    public UserResponse create(SaveUserRequest request) {
        validateUniqueFields(request);
        UserResponse response = UserResponse.fromUser(save(request, getRegularUserAuthorities()));
        log.info("User with email {} was registered as regular user", request.email());
        return response;
    }

    @Override
    @Transactional
    public UserResponse editByEmail(String email, ChangeUserInfoRequest request) {
        User dbUser = userRepository.findByEmail(email)
            .orElseThrow(() -> UserOperationExceptions.userWithEmailNotFound(email));
        dbUser.setEmail(request.email());
        dbUser.setName(request.name());
        dbUser.setSurname(request.surname());
        dbUser.setPhone(request.phone());
        userRepository.save(dbUser);
        return UserResponse.fromUser(dbUser);
    }

    @Override
    @Transactional
    public UserResponse topUp(String email, TopUpAccountRequest request) {
        User dbUser = userRepository.findByEmail(email)
            .orElseThrow(() -> UserOperationExceptions.userWithEmailNotFound(email));
        /*
         * NEEDED IMPLEMENTATION OF PAYMENT SERVICE
         * */
        dbUser.setBalance(dbUser.getBalance().add(new BigDecimal(request.addedSum())));
        userRepository.save(dbUser);
        log.info("Top up balance of user {} with {}", email, request.addedSum());
        return UserResponse.fromUser(dbUser);
    }

    @Override
    @Transactional
    public UserResponse changePasswordByEmail(String email, ChangeUserPasswordRequest request) {
        User dbUser = userRepository.findByEmail(email)
            .orElseThrow(() -> UserOperationExceptions.userWithEmailNotFound(email));
        if (!passwordEncoder.matches(request.oldPassword(), dbUser.getPassword())) {
            throw UserOperationExceptions.incorrectPassword("Old password is not correct");
        }
        dbUser.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(dbUser);
        log.info("Password of user with email {} was changed", email);
        return UserResponse.fromUser(dbUser);
    }

    @Override
    @Transactional
    public UserResponse changePasswordById(String id, String newPassword) {
        User user = userRepository.findById(UUID.fromString(id))
            .orElseThrow(() -> UserOperationExceptions.userWithIdNotFound(id));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        log.info("Password of user with id {} was changed by admin", id);
        return UserResponse.fromUser(user);
    }

    @Override
    @Transactional
    public UserResponse createAdmin(SaveUserRequest request) {
        validateUniqueFields(request);
        UserResponse response = UserResponse.fromUser(
            save(request, getUpperAuthorities(AuthorityRepository.ADMIN_AUTHORITIES)));
        log.info("User with email {} was registered as admin", request.email());
        return response;
    }

    @Override
    @Transactional
    public UserResponse createOwner(SaveUserRequest request) {
        validateUniqueFields(request);
        UserResponse response = UserResponse.fromUser(
            save(request, getUpperAuthorities(AuthorityRepository.OWNER_AUTHORITIES)));
        log.info("User with email {} was registered as owner", request.email());
        return response;
    }

    @Override
    @Transactional
    public UserResponse changeStatusById(String id, UserStatus status) {
        User user = userRepository.findById(UUID.fromString(id))
            .orElseThrow(() -> UserOperationExceptions.userWithIdNotFound(id));
        if (user.getStatus() != status) {
            user.setStatus(status);
            log.info("Status of the user with id {} was changed to {}", id, status.toString());
        }
        return UserResponse.fromUser(user);
    }

    @Override
    @Transactional
    public UserResponse changeStatusById(String id, String stringStatus) {
        try {
            UserStatus status = UserStatus.valueOf(stringStatus);
            return changeStatusById(id, status);
        } catch (IllegalArgumentException exception) {
            throw UserOperationExceptions.statusUnsupported(stringStatus);
        }
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        UUID dbId = UUID.fromString(id);
        if (!userRepository.existsById(dbId)) {
            throw UserOperationExceptions.userWithIdNotFound(id);
        }
        log.warn("User with id {} was removed", id);
        userRepository.deleteById(dbId);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found"));
        return new AuthUserDetails(user);
    }

    @Transactional
    public void mergeAdmins(List<SaveUserRequest> requests) {
        if (requests.isEmpty()) {
            return;
        }
        Map<KnownAuthority, UserAuthority> authorities = getUpperAuthorities(
            AuthorityRepository.OWNER_AUTHORITIES);
        for (SaveUserRequest request : requests) {
            String email = request.email();
            String name = request.name();
            User user = userRepository.findByEmail(email).orElseGet(() -> {
                var newUser = new User();
                newUser.setEmail(email);
                newUser.setBalance(new BigDecimal(0));
                return newUser;
            });
            user.setName(name);
            user.setPassword(passwordEncoder.encode(request.password()));
            user.getAuthorities().putAll(authorities);
            userRepository.save(user);
        }

    }

    private void validateUniqueFields(SaveUserRequest request) {
        String email = request.email();
        if (userRepository.existsByEmail(email)) {
            throw UserOperationExceptions.duplicateEmail(email);
        }
    }

    private User save(SaveUserRequest request, Map<KnownAuthority, UserAuthority> authorities) {
        var user = new User();
        user.getAuthorities().putAll(authorities);
        user.setEmail(request.email());
        user.setName(request.name());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setBalance(new BigDecimal(0));
        userRepository.save(user);
        return user;
    }

    private Map<KnownAuthority, UserAuthority> getUpperAuthorities(Set<KnownAuthority> authoritySet) {
        return authorityRepository.findAllByIdIn(authoritySet)
            .collect(Collectors.toMap(UserAuthority::getId, Function.identity(), (e1, e2) -> e2,
                                      () -> new EnumMap<>(KnownAuthority.class)));
    }

    private Map<KnownAuthority, UserAuthority> getRegularUserAuthorities() {
        UserAuthority authority = authorityRepository.findById(KnownAuthority.ROLE_USER)
            .orElseThrow(() -> UserOperationExceptions.authorityNotFound(KnownAuthority.ROLE_USER.name()));
        Map<KnownAuthority, UserAuthority> authorities = new EnumMap<>(KnownAuthority.class);
        authorities.put(KnownAuthority.ROLE_USER, authority);
        return authorities;
    }

}
