package com.chaplygin.task_manager.auth.controller;

import com.chaplygin.task_manager.auth.dto.SignInResponse;
import com.chaplygin.task_manager.auth.dto.SignUpResponse;
import com.chaplygin.task_manager.auth.service.AuthService;
import com.chaplygin.task_manager.testDataFactory.SignInRequestFactory;
import com.chaplygin.task_manager.testDataFactory.SignUpRequestFactory;
import com.chaplygin.task_manager.user.dto.SignInRequest;
import com.chaplygin.task_manager.user.dto.SignUpRequest;
import com.chaplygin.task_manager.user.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @Test
    public void givenValidSignUpRequest_whenSignUp_thenReturnCreated() throws Exception {
        SignUpRequest signUpRequest = SignUpRequestFactory.createSignUpRequest();
        SignUpResponse signUpResponse = new SignUpResponse();
        given(authService.signUp(any(User.class)))
                .willReturn(signUpResponse);

        String jsonRequest = objectMapper.writeValueAsString(signUpRequest);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/auth/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest)
                )
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void givenInvalidSignUpRequest_whenSignUp_thenReturnBadRequest() throws Exception {
        SignUpRequest nullEmailSignUpRequest = SignUpRequestFactory.createSignUpRequestNullEmail();
        SignUpResponse signUpResponse = new SignUpResponse();
        given(authService.signUp(any(User.class)))
                .willReturn(signUpResponse);

        String nullEmailJsonRequest = objectMapper.writeValueAsString(nullEmailSignUpRequest);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/auth/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(nullEmailJsonRequest)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void givenValidSignInRequest_whenSignUp_thenReturnCreated() throws Exception {
        SignInRequest signInRequest = SignInRequestFactory.createSignInRequest();
        SignInResponse signInResponse = new SignInResponse();
        given(authService.signIn(any(User.class)))
                .willReturn(signInResponse);

        String jsonRequest = objectMapper.writeValueAsString(signInRequest);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/auth/signin")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest)
                )
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void givenInvalidSignInRequest_whenSignUp_thenReturnBadRequest() throws Exception {
        SignInRequest nullEmailSignInRequest = SignInRequestFactory.createSignInRequestNullEmail();
        SignInResponse signInResponse = new SignInResponse();
        given(authService.signIn(any(User.class)))
                .willReturn(signInResponse);

        String nullEmailJsonRequest = objectMapper.writeValueAsString(nullEmailSignInRequest);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/auth/signin")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(nullEmailJsonRequest)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}