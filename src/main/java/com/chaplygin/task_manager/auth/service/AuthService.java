package com.chaplygin.task_manager.auth.service;

import com.chaplygin.task_manager.auth.dto.SignInResponse;
import com.chaplygin.task_manager.auth.dto.SignUpResponse;
import com.chaplygin.task_manager.user.mapper.UserMapper;
import com.chaplygin.task_manager.user.model.User;
import com.chaplygin.task_manager.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    @Transactional
    public SignUpResponse signUp(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userService.createUser(user);
        String accessToken = jwtService.generateAccessToken(savedUser.getEmail(), savedUser.getRole());

        return SignUpResponse.builder()
                .user(userMapper.mapUserToUserResponseDtoFull(savedUser))
                .accessToken(accessToken)
                .build();
    }

    @Transactional
    public SignInResponse signIn(User user) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                user.getEmail(),
                user.getPassword()
        ));
        User foundUser = userService.findByEmail(user.getEmail()).orElseThrow(
                () -> new UsernameNotFoundException("User '%s' not found".formatted(user.getEmail()))
        );

        String accessToken = jwtService.generateAccessToken(foundUser.getEmail(), foundUser.getRole()
        );

        return SignInResponse.builder()
                .accessToken(accessToken)
                .build();
    }
}
