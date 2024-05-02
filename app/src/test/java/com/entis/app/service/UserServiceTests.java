package com.entis.app.service;

import com.entis.app.entity.user.KnownAuthority;
import com.entis.app.entity.user.User;
import com.entis.app.entity.user.UserAuthority;
import com.entis.app.entity.user.UserStatus;
import com.entis.app.entity.user.request.ChangeUserInfoRequest;
import com.entis.app.entity.user.request.ChangeUserPasswordRequest;
import com.entis.app.entity.user.request.TopUpAccountRequest;
import com.entis.app.entity.user.response.UserResponse;
import com.entis.app.repository.AuthorityRepository;
import com.entis.app.repository.ChargeRepository;
import com.entis.app.repository.UserRepository;
import com.entis.app.service.user.impl.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

public class UserServiceTests {

    private UserService userService;
    private UserRepository userRepository;
    private AuthorityRepository authorityRepository;
    private ChargeRepository chargeRepository;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder(10, new SecureRandom());
        userRepository = mock(UserRepository.class);
        authorityRepository = mock(AuthorityRepository.class);
        chargeRepository = mock(ChargeRepository.class);
        userService = new UserService(userRepository, authorityRepository, chargeRepository, passwordEncoder);
    }

    @Test
    void testFindById() {
        var absentId = UUID.randomUUID();
        var presentId = UUID.randomUUID();
        User user = createUser(presentId, "email@da", "1");

        when(userRepository.findById(absentId)).thenReturn(Optional.empty());
        when(userRepository.findById(presentId)).thenReturn(Optional.of(user));

        Optional<UserResponse> absentResponse = userService.findById(absentId.toString());

        assertThat(absentResponse).isEmpty();
        verify(userRepository).findById(absentId);

        Optional<UserResponse> presentResponse = userService.findById(presentId.toString());

        assertThat(presentResponse).hasValueSatisfying(userResponse ->
                assertUserMatchesUserResponse(user, userResponse));
        verify(userRepository).findById(presentId);

        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void testFindByEmail() {
        String absentEmail = UUID.randomUUID() + "@gmail.com";
        String presentEmail = UUID.randomUUID() + "@gmail.com";
        User user = createUser(UUID.randomUUID(), presentEmail, "1");

        when(userRepository.findByEmail(absentEmail)).thenReturn(Optional.empty());
        when(userRepository.findByEmail(presentEmail)).thenReturn(Optional.of(user));

        Optional<UserResponse> absentResponse = userService.findByEmail(absentEmail);

        assertThat(absentResponse).isEmpty();
        verify(userRepository).findByEmail(absentEmail);

        Optional<UserResponse> presentResponse = userService.findByEmail(presentEmail);

        assertThat(presentResponse).hasValueSatisfying(userResponse ->
                assertUserMatchesUserResponse(user, userResponse));
        verify(userRepository).findByEmail(presentEmail);

        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void testInfoUpdateByEmail() {
        String absentEmail = UUID.randomUUID() + "@gmail.com";
        String presentEmail = UUID.randomUUID() + "@gmail.com";
        ChangeUserInfoRequest request = new ChangeUserInfoRequest(presentEmail, "newName", "surname", null);
        User user = createUser(UUID.randomUUID(), presentEmail, "1");

        when(userRepository.findByEmail(absentEmail)).thenReturn(Optional.empty());
        when(userRepository.findByEmail(presentEmail)).thenReturn(Optional.of(user));
        when(userRepository.save(same(user))).thenReturn(user);

        assertThatExceptionOfType(ResponseStatusException.class)
                .isThrownBy(() -> userService.editByEmail(absentEmail, request))
                .satisfies(e -> assertThat(e.getStatusCode()).isSameAs(HttpStatus.NOT_FOUND));

        verify(userRepository).findByEmail(absentEmail);

        userService.editByEmail(presentEmail, request);

        assertThat(user.getName()).isEqualTo(request.name());
        assertThat(user.getEmail()).isEqualTo(request.email());
        assertThat(user.getPhone()).isEqualTo(request.phone());
        assertThat(user.getSurname()).isEqualTo(request.surname());

        verify(userRepository).findByEmail(presentEmail);
        verify(userRepository).save(same(user));

        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void testChangePassword() {
        String absentEmail = UUID.randomUUID() + "@gmail.com";
        String presentEmail = UUID.randomUUID() + "@gmail.com";
        String oldPassword = "1";
        ChangeUserPasswordRequest request = new ChangeUserPasswordRequest(oldPassword, "qwerty2004");
        User user = createUser(UUID.randomUUID(), presentEmail, oldPassword);

        when(userRepository.findByEmail(absentEmail)).thenReturn(Optional.empty());
        when(userRepository.findByEmail(presentEmail)).thenReturn(Optional.of(user));
        when(userRepository.save(same(user))).thenReturn(user);

        assertThatExceptionOfType(ResponseStatusException.class)
                .isThrownBy(() -> userService.changePasswordByEmail(absentEmail, request))
                .satisfies(e -> assertThat(e.getStatusCode()).isSameAs(HttpStatus.NOT_FOUND));

        verify(userRepository).findByEmail(absentEmail);

        Assertions.assertTrue(passwordEncoder.matches(request.oldPassword(), user.getPassword()));

        userService.changePasswordByEmail(presentEmail, request);

        Assertions.assertTrue(passwordEncoder.matches(request.newPassword(), user.getPassword()));

        verify(userRepository).findByEmail(presentEmail);
        verify(userRepository).save(same(user));

        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void testTopUp() {
        String absentEmail = UUID.randomUUID() + "@gmail.com";
        String presentEmail = UUID.randomUUID() + "@gmail.com";
        TopUpAccountRequest floatRequest = new TopUpAccountRequest("17.004");
        TopUpAccountRequest integerRequest = new TopUpAccountRequest("228");
        User user = createUser(UUID.randomUUID(), presentEmail, "1");

        when(userRepository.findByEmail(absentEmail)).thenReturn(Optional.empty());
        when(userRepository.findByEmail(presentEmail)).thenReturn(Optional.of(user));
        when(userRepository.save(same(user))).thenReturn(user);

        assertThatExceptionOfType(ResponseStatusException.class)
                .isThrownBy(() -> userService.topUp(absentEmail, floatRequest))
                .satisfies(e -> assertThat(e.getStatusCode()).isSameAs(HttpStatus.NOT_FOUND));

        verify(userRepository).findByEmail(absentEmail);

        userService.topUp(presentEmail, floatRequest);

        assertThat(user.getBalance()).isEqualTo(new BigDecimal(floatRequest.addedSum()));

        userService.topUp(presentEmail, integerRequest);

        assertThat(user.getBalance()).isEqualTo(new BigDecimal("245.004"));
    }

    private static void assertUserMatchesUserResponse(User user, UserResponse response) {
        assertThat(user.getId().toString()).isEqualTo(response.id());
        assertThat(user.getStatus()).isEqualTo(response.status());
        assertThat(user.getEmail()).isEqualTo(response.email());
        assertThat(user.getName()).isEqualTo(response.name());
        assertThat(user.getSurname()).isEqualTo(response.surname());
        assertThat(user.getPhone()).isEqualTo(response.phone());
        assertThat(user.getBalance().doubleValue()).isEqualTo(response.balance());
        assertThat(user.getAuthorities().size()).isEqualTo(response.authorities().size());
    }

    private User createUser(UUID id, String email, String password) {
        User user = new User();
        user.setName("user");
        user.setEmail(email);
        user.setBalance(new BigDecimal(0));
        user.setPassword(passwordEncoder.encode(password));
        user.setStatus(UserStatus.ACTIVE);
        user.setId(id);
        UserAuthority testAuth = new UserAuthority();
        testAuth.setId(KnownAuthority.ROLE_USER);
        user.getAuthorities().put(KnownAuthority.ROLE_USER, testAuth);
        return user;
    }
}
