package com.chaplygin.task_manager.user.repository;

import com.chaplygin.task_manager.testDataFactory.UserFactory;
import com.chaplygin.task_manager.user.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@Testcontainers
@ActiveProfiles("test")
class UserRepositoryTest {

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");
    @Autowired
    private UserRepository userRepository;

    @DynamicPropertySource
    static void registerDatabaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () ->
                String.format("jdbc:postgresql://localhost:%d/%s",
                        postgresContainer.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT),
                        postgresContainer.getDatabaseName()));
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @Test
    @Transactional
    public void givenValidUser_whenSave_thenReturnUser() {
        User user = UserFactory.createUser1FromRequestPrepared();

        User savedUser = userRepository.save(user);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    @Transactional
    public void givenUser_WithNullEmail_whenSave_thenThrowException() {
        User userWithNullEmail = UserFactory.createUser1WithNullEmailFromRequestPrepared();

        assertThatThrownBy(() -> userRepository.save(userWithNullEmail))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("null value");
    }

    @Test
    @Transactional
    public void givenUser_WithNullUserName_whenSave_thenThrowException() {
        User userWithNullUserName = UserFactory.createUser1WithNullUserNameFromRequestPrepared();

        assertThatThrownBy(() -> userRepository.save(userWithNullUserName))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("null value");
    }

    @Test
    @Transactional
    public void givenUser_WithNullRole_whenSave_thenThrowException() {
        User userWithNullUserRole = UserFactory.createUser1WithNullRoleFromRequestPrepared();

        assertThatThrownBy(() -> userRepository.save(userWithNullUserRole))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("null value");
    }

    @Test
    @Transactional
    public void givenUser_WithNullPassword_whenSave_thenThrowException() {
        User userWithNullUserPassword = UserFactory.createUser1WithNullPasswordFromRequestPrepared();

        assertThatThrownBy(() -> userRepository.save(userWithNullUserPassword))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("null value");
    }

    @Test
    @Transactional
    public void givenUser_WithNonUniqueEmail_whenSave_thenThrowException() {
        User user1 = UserFactory.createUser1FromRequestPrepared();
        userRepository.save(user1);

        User user2NonUniqueEmail = UserFactory.createUser2FromRequestPreparedNonUniqueEmail();

        assertThatThrownBy(() -> userRepository.save(user2NonUniqueEmail))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("duplicate key");
    }

    @Test
    @Transactional
    public void givenUser_WithNonUniqueUserName_whenSave_thenThrowException() {
        User user1 = UserFactory.createUser1FromRequestPrepared();
        userRepository.save(user1);

        User user2NonUniqueUserName = UserFactory.createUser2FromRequestPreparedNonUniqueUserName();

        assertThatThrownBy(() -> userRepository.save(user2NonUniqueUserName))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("duplicate key");
    }

    @Test
    @Transactional
    public void givenExistingEmail_whenFindByEmail_shouldReturnUser() {
        User user = UserFactory.createUser1FromRequestPrepared();
        userRepository.save(user);

        Optional<User> userOptional = userRepository.findByEmail(user.getEmail());

        assertThat(userOptional.isPresent()).isTrue();
        assertThat(userOptional.get().getEmail()).isEqualTo(user.getEmail());
    }
}