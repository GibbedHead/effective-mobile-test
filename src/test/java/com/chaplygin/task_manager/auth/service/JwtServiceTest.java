package com.chaplygin.task_manager.auth.service;

import com.chaplygin.task_manager.user.model.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtService, "jwtSecret", "faadd63584c84e672ff5824b4f85226faadd63584c84e672ff5824b4f85226");
        ReflectionTestUtils.setField(jwtService, "accessTokenExpiration", Duration.ofMinutes(10));
    }

    @Test
    void givenEmailAndRoles_whenGenerateAccessToken_thenReturnAccessTokenWithEmailAsSubjAndGivenRoles() {
        String email = "user1@doamin.com";
        Role role = Role.ROLE_USER;

        String token = jwtService.generateAccessToken(email, role);

        assertThat(token).isNotBlank();


        Claims claims = Jwts.parser()
                .verifyWith(jwtService.getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        assertThat(claims.getSubject()).isEqualTo(email);
        assertThat(claims.get("roles")).isEqualTo(List.of(role.toString()));
    }

    @Test
    public void givenValidToken_whenExtractEmail_thenReturnEmail() {
        String email = "user1@doamin.com";
        Role role = Role.ROLE_USER;

        String token = jwtService.generateAccessToken(email, role);

        String extractedEmail = jwtService.extractEmail(token);

        assertThat(extractedEmail).isEqualTo(email);
    }
}