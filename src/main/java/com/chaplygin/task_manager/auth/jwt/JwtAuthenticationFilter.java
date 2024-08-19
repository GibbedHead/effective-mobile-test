package com.chaplygin.task_manager.auth.jwt;

import com.chaplygin.task_manager.auth.service.JwtService;
import com.chaplygin.task_manager.exception.model.AccessTokenExpiredException;
import com.chaplygin.task_manager.exception.model.AccessTokenMalformedException;
import com.chaplygin.task_manager.exception.model.AccessTokenSignatureException;
import com.chaplygin.task_manager.exception.model.AppErrorResponse;
import com.chaplygin.task_manager.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String AUTHORIZATION_HEADER_NAME = "Authorization";

    private final JwtService jwtService;
    private final ObjectMapper objectMapper;
    private final UserService userService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(AUTHORIZATION_HEADER_NAME);
        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = authHeader.substring(BEARER_PREFIX.length());
        String email;

        try {
            email = jwtService.extractEmail(accessToken);
        } catch (SignatureException e) {
            handleException(response, new AccessTokenSignatureException("Invalid access token"), HttpStatus.FORBIDDEN);
            return;
        } catch (ExpiredJwtException e) {
            handleException(response, new AccessTokenExpiredException("Access token expired"), HttpStatus.UNAUTHORIZED);
            return;
        } catch (MalformedJwtException e) {
            handleException(response, new AccessTokenMalformedException("Access token malformed"), HttpStatus.FORBIDDEN);
            return;
        }

        if (email != null && !email.isBlank() && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userService.loadUserByUsername(email);

            SecurityContext context = SecurityContextHolder.createEmptyContext();

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            context.setAuthentication(authToken);
            SecurityContextHolder.setContext(context);
        }

        filterChain.doFilter(request, response);
    }

    private void handleException(HttpServletResponse response, Exception exception, HttpStatus status) throws IOException {
        AppErrorResponse appErrorResponse = new AppErrorResponse(status.value(), exception.getMessage());
        String jsonResponse = objectMapper.writeValueAsString(appErrorResponse);

        response.setStatus(status.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }
}
