package com.chaplygin.task_manager.user.service;

import com.chaplygin.task_manager.exception.model.InvalidUserException;
import com.chaplygin.task_manager.user.model.Role;
import com.chaplygin.task_manager.user.model.User;
import com.chaplygin.task_manager.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private static final Role ROLE_USER = Role.ROLE_USER;

    private final UserRepository userRepository;

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public User createUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new InvalidUserException("Username '%s' already exists".formatted(user.getUsername()));
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new InvalidUserException("Email '%s' already exists".formatted(user.getEmail()));
        }

        user.setRole(ROLE_USER);
        return saveUser(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("User '%s' not found".formatted(email))
        );
        return mapUserToUserDetails(user);
    }

    private UserDetails mapUserToUserDetails(User user) {
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getAuthorities()
        );
    }

}
