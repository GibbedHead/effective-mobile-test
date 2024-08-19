package com.chaplygin.task_manager.user.service;

import com.chaplygin.task_manager.exception.model.InvalidUserException;
import com.chaplygin.task_manager.testDataFactory.UserFactory;
import com.chaplygin.task_manager.user.model.User;
import com.chaplygin.task_manager.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void givenNewUser_whenSaveUser_thenReturnSavedUser() {
        User userToSave = UserFactory.createUser1FromRequest();
        User userFromRepository = UserFactory.createUser1Saved();
        given(userRepository.save(any(User.class)))
                .willReturn(userFromRepository);

        User savedUser = userService.saveUser(userToSave);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo(userToSave.getUsername());
    }

    @Test
    public void givenExistingUsernameSignUpRequest_whenCreateUser_thenThrowException() {
        given(userRepository.existsByUsername(any(String.class)))
                .willReturn(true);

        assertThatThrownBy(() -> userService.createUser(UserFactory.createUser1FromRequest()))
                .isInstanceOf(InvalidUserException.class);

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void givenExistingEmailSignUpRequest_whenCreateUser_thenThrowException() {
        given(userRepository.existsByEmail(any()))
                .willReturn(true);

        assertThatThrownBy(() -> userService.createUser(UserFactory.createUser1FromRequest()))
                .isInstanceOf(InvalidUserException.class);

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void givenNewUser_whenCreateUser_thenReturnSavedUser() {
        given(userRepository.save(any(User.class)))
                .willReturn(UserFactory.createUser1Saved());

        User createdUser = userService.createUser(UserFactory.createUser1FromRequest());

        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getUsername()).isEqualTo("User1");
        assertThat(createdUser.getEmail()).isEqualTo("user1@domain.com");
    }

    @Test
    public void givenNonExistingUsername_whenFindByEmail_thenReturnEmpty() {
        String email = "nonExistingEmail@doamin.com";

        given(userRepository.findByEmail(email))
                .willReturn(Optional.empty());

        Optional<User> foundUser = userService.findByEmail(email);

        assertThat(foundUser).isNotPresent();
    }

    @Test
    public void givenExistingUsername_whenFindByEmail_thenReturnUser() {
        String email = UserFactory.createUser1FromRequest().getEmail();

        given(userRepository.findByEmail(email))
                .willReturn(Optional.of(UserFactory.createUser1Saved()));

        Optional<User> foundUser = userService.findByEmail(email);

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo(email);
    }

    @Test
    public void givenNonExistingUsername_whenLoadUserByUsername_thenThrowException() {
        String email = "nonExistingEmail@doamin.com";

        given(userRepository.findByEmail(email))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> userService.loadUserByUsername(email))
                .isInstanceOf(UsernameNotFoundException.class);
    }

    @Test
    public void givenExistingEmail_whenLoadUserByEmail_thenReturnUserDetails() {
        User user = UserFactory.createUser1FromRequest();


        given(userRepository.findByEmail(user.getUsername()))
                .willReturn(Optional.of(UserFactory.createUser1Saved()));

        UserDetails userDetails = userService.loadUserByUsername(user.getUsername());

        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo(user.getUsername());
    }
}