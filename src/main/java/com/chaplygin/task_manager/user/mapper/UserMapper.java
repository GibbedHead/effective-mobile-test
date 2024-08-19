package com.chaplygin.task_manager.user.mapper;

import com.chaplygin.task_manager.user.dto.SignInRequest;
import com.chaplygin.task_manager.user.dto.SignUpRequest;
import com.chaplygin.task_manager.user.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User mapSignUpRequestToUser(SignUpRequest signUpRequest);

    User mapSignInRequestToUser(SignInRequest signInRequest);
}
