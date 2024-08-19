package com.chaplygin.task_manager.user.dto;

import com.chaplygin.task_manager.testDataFactory.SignInRequestFactory;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class SignInRequestTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void givenNullEmailSignInRequest_whenValidate_thenNotValid() {
        Set<ConstraintViolation<SignInRequest>> constraintViolations = validator.validate(
                SignInRequestFactory.createSignInRequestNullEmail()
        );

        assertThat(constraintViolations.size()).isEqualTo(1);
    }

    @Test
    public void givenNullPasswordSignInRequest_whenValidate_thenNotValid() {
        Set<ConstraintViolation<SignInRequest>> constraintViolations = validator.validate(
                SignInRequestFactory.createSignInRequestNullPassword()
        );

        assertThat(constraintViolations.size()).isEqualTo(1);
    }


    @Test
    public void givenEmailShorterThan6CharactersSignInRequest_whenValidate_thenNotValid() {
        Set<ConstraintViolation<SignInRequest>> constraintViolations = validator.validate(
                SignInRequestFactory.createSignInRequestEmailShorterThan6Characters()
        );

        assertThat(constraintViolations.size()).isEqualTo(1);
    }

    @Test
    public void givenEmailLongerThan50CharactersSignInRequest_whenValidate_thenNotValid() {
        Set<ConstraintViolation<SignInRequest>> constraintViolations = validator.validate(
                SignInRequestFactory.createSignInRequestEmailLongerThan50Characters()
        );

        assertThat(constraintViolations.size()).isEqualTo(1);
    }

    @Test
    public void givenPasswordShorterThan1CharactersSignInRequest_whenValidate_thenNotValid() {
        Set<ConstraintViolation<SignInRequest>> constraintViolations = validator.validate(
                SignInRequestFactory.createSignInRequestPasswordShorterThan1Characters()
        );

        assertThat(constraintViolations.size()).isEqualTo(2);
    }

    @Test
    public void givenEmailLongerThan20CharactersSignInRequest_whenValidate_thenNotValid() {
        Set<ConstraintViolation<SignInRequest>> constraintViolations = validator.validate(
                SignInRequestFactory.createSignInRequestPasswordLongerThan20Characters()
        );

        assertThat(constraintViolations.size()).isEqualTo(1);
    }

}