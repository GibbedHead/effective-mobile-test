package com.chaplygin.task_manager.user.mapper;

import com.chaplygin.task_manager.testDataFactory.SignInRequestFactory;
import com.chaplygin.task_manager.testDataFactory.SignUpRequestFactory;
import com.chaplygin.task_manager.user.dto.SignInRequest;
import com.chaplygin.task_manager.user.dto.SignUpRequest;
import com.chaplygin.task_manager.user.model.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    public void givenNullSignUpRequest_whenMapToUser_thenReturnNull() {
        User user = userMapper.mapSignUpRequestToUser(null);

        assertThat(user).isNull();
    }

    @Test
    public void givenNullSignInRequest_whenMapToUser_thenReturnNull() {
        User user = userMapper.mapSignInRequestToUser(null);

        assertThat(user).isNull();
    }

    @Test
    public void givenSignUpRequest_whenMapToUser_thenReturnUser() {
        SignUpRequest signUpRequest = SignUpRequestFactory.createSignUpRequest();

        User user = userMapper.mapSignUpRequestToUser(signUpRequest);

        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo(signUpRequest.getUsername());
        assertThat(user.getPassword()).isEqualTo(signUpRequest.getPassword());
        assertThat(user.getEmail()).isEqualTo(signUpRequest.getEmail());
    }

    @Test
    public void givenSignInRequest_whenMapToUser_thenReturnUser() {
        SignInRequest signInRequest = SignInRequestFactory.createSignInRequest();

        User user = userMapper.mapSignInRequestToUser(signInRequest);

        assertThat(user).isNotNull();
        assertThat(user.getPassword()).isEqualTo(signInRequest.getPassword());
        assertThat(user.getEmail()).isEqualTo(signInRequest.getEmail());
    }
}