package com.chaplygin.task_manager.user.dto;

import com.chaplygin.task_manager.testDataFactory.SignUpRequestFactory;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class SignUpRequestTest {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void givenNullEmailSignUpRequest_whenValidate_thenNotValid() {
        Set<ConstraintViolation<SignUpRequest>> constraintViolations = validator.validate(
                SignUpRequestFactory.createSignUpRequestNullEmail()
        );

        assertThat(constraintViolations.size()).isEqualTo(1);
    }

    @Test
    public void givenNullPasswordSignUpRequest_whenValidate_thenNotValid() {
        Set<ConstraintViolation<SignUpRequest>> constraintViolations = validator.validate(
                SignUpRequestFactory.createSignUpRequestNullPassword()
        );

        assertThat(constraintViolations.size()).isEqualTo(1);
    }

    @Test
    public void givenNullUserNameSignUpRequest_whenValidate_thenNotValid() {
        Set<ConstraintViolation<SignUpRequest>> constraintViolations = validator.validate(
                SignUpRequestFactory.createSignUpRequestNullUserName()
        );

        assertThat(constraintViolations.size()).isEqualTo(1);
    }

    @Test
    public void givenEmailShorterThan6CharactersSignUpRequest_whenValidate_thenNotValid() {
        Set<ConstraintViolation<SignUpRequest>> constraintViolations = validator.validate(
                SignUpRequestFactory.createSignUpRequestEmailShorterThan6Characters()
        );

        assertThat(constraintViolations.size()).isEqualTo(1);
    }

    @Test
    public void givenEmailLongerThan50CharactersSignUpRequest_whenValidate_thenNotValid() {
        Set<ConstraintViolation<SignUpRequest>> constraintViolations = validator.validate(
                SignUpRequestFactory.createSignUpRequestEmailLongerThan50Characters()
        );

        assertThat(constraintViolations.size()).isEqualTo(1);
    }

    @Test
    public void givenPasswordShorterThan1CharactersSignUpRequest_whenValidate_thenNotValid() {
        Set<ConstraintViolation<SignUpRequest>> constraintViolations = validator.validate(
                SignUpRequestFactory.createSignUpRequestPasswordShorterThan1Characters()
        );

        assertThat(constraintViolations.size()).isEqualTo(2);
    }

    @Test
    public void givenEmailLongerThan20CharactersSignUpRequest_whenValidate_thenNotValid() {
        Set<ConstraintViolation<SignUpRequest>> constraintViolations = validator.validate(
                SignUpRequestFactory.createSignUpRequestPasswordLongerThan20Characters()
        );

        assertThat(constraintViolations.size()).isEqualTo(1);
    }

    @Test
    public void givenUserNameShorterThan3CharactersSignUpRequest_whenValidate_thenNotValid() {
        Set<ConstraintViolation<SignUpRequest>> constraintViolations = validator.validate(
                SignUpRequestFactory.createSignUpRequestUserNameShorterThan3Characters()
        );

        assertThat(constraintViolations.size()).isEqualTo(1);
    }

    @Test
    public void givenUserNameLongerThan50CharactersSignUpRequest_whenValidate_thenNotValid() {
        Set<ConstraintViolation<SignUpRequest>> constraintViolations = validator.validate(
                SignUpRequestFactory.createSignUpRequestUserNameLongerThan50Characters()
        );

        assertThat(constraintViolations.size()).isEqualTo(1);
    }
}