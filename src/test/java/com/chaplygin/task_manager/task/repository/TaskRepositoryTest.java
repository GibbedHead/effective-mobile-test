package com.chaplygin.task_manager.task.repository;

import com.chaplygin.task_manager.task.model.Task;
import com.chaplygin.task_manager.task.specification.TaskSpecifications;
import com.chaplygin.task_manager.testDataFactory.TaskFactory;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@Testcontainers
@ActiveProfiles("test")
class TaskRepositoryTest {

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");
    @Autowired
    private TaskRepository taskRepository;

    @DynamicPropertySource
    static void registerDatabaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () ->
                String.format("jdbc:postgresql://localhost:%d/%s",
                        postgresContainer.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT),
                        postgresContainer.getDatabaseName()));
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @Transactional
    @Test
    public void givenValidTask_whenSave_thenReturnTask() {
        Task task = TaskFactory.createInvalidNewTask();
        assertThatThrownBy(() -> taskRepository.save(task))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    public void testFindAllTasksWithSpecification() {
        taskRepository.save(TaskFactory.createNewTask1());
        taskRepository.save(TaskFactory.createNewTask2());

        Specification<Task> spec = Specification.where(TaskSpecifications.hasTitle("Test task1"));
        Pageable pageable = PageRequest.of(0, 10);
        Page<Task> page = taskRepository.findAll(spec, pageable);

        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.getContent().getFirst().getTitle()).isEqualTo("Test task1");
    }
}