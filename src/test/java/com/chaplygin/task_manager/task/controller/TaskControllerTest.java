package com.chaplygin.task_manager.task.controller;

import com.chaplygin.task_manager.exception.model.TaskNotFoundException;
import com.chaplygin.task_manager.task.dto.TaskCreateDto;
import com.chaplygin.task_manager.task.dto.TaskPagedListResponseDto;
import com.chaplygin.task_manager.task.dto.TaskResponseDtoNoComments;
import com.chaplygin.task_manager.task.mapper.TaskListMapper;
import com.chaplygin.task_manager.task.mapper.TaskMapper;
import com.chaplygin.task_manager.task.model.Task;
import com.chaplygin.task_manager.task.service.TaskService;
import com.chaplygin.task_manager.testDataFactory.TaskFactory;
import com.chaplygin.task_manager.testDataFactory.UserFactory;
import com.chaplygin.task_manager.user.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTest {

    private final static String TASKS_URL = "/api/v1/tasks";
    private final static String TASK_URL = "/api/v1/tasks/1";
    private final static String USER_TASKS_URL = TASKS_URL + "/user/1";
    private final static String OWNER_TASKS_URL = TASKS_URL + "/owner/1";
    private final static String ASSIGNEE_TASKS_URL = TASKS_URL + "/assignee/1";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskService taskService;
    @MockBean
    private TaskMapper taskMapper;
    @MockBean
    private TaskListMapper taskListMapper;

    @BeforeEach
    void setUp() {
        User user = UserFactory.createUser1Saved();
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities())
        );
    }

    @Test
    public void givenValidTaskCreateDto_whenCreateTask_thenReturnCreated() throws Exception {
        TaskCreateDto taskCreateDto = TaskFactory.createValidTaskCreateDto();
        TaskResponseDtoNoComments taskResponseDto = TaskFactory.createTaskResponseDtoNoComments();
        Task task = TaskFactory.createTask();

        given(taskService.saveTask(any(Task.class)))
                .willReturn(task);
        given(taskMapper.mapTaskToResponseDtoNoComments(any(Task.class)))
                .willReturn(taskResponseDto);
        given(taskMapper.mapCreateDtoToTask(any(TaskCreateDto.class)))
                .willReturn(task);
        String jsonRequest = objectMapper.writeValueAsString(taskCreateDto);

        mockMvc.perform(
                        MockMvcRequestBuilders.post(TASKS_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest)
                )
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void givenInvalidTaskCreateDto_whenCreateTask_thenReturnCreated() throws Exception {
        TaskCreateDto invalidTaskCreateDto = TaskFactory.createInvalidTaskCreateDto();
        TaskResponseDtoNoComments taskResponseDto = TaskFactory.createTaskResponseDtoNoComments();
        Task task = TaskFactory.createTask();

        given(taskService.saveTask(any(Task.class)))
                .willReturn(task);
        given(taskMapper.mapTaskToResponseDtoNoComments(any(Task.class)))
                .willReturn(taskResponseDto);
        given(taskMapper.mapCreateDtoToTask(any(TaskCreateDto.class)))
                .willReturn(task);
        String jsonRequest = objectMapper.writeValueAsString(invalidTaskCreateDto);

        mockMvc.perform(
                        MockMvcRequestBuilders.post(TASKS_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void givenValidParameters_whenGetAllTasks_thenReturnOk() throws Exception {
        Page<Task> taskPage = TaskFactory.createTaskPage();
        TaskPagedListResponseDto taskPagedListResponseDto = TaskFactory.createTaskPagedListResponseDto();

        given(taskService.getAllTasks(anyInt(), anyInt(), anyString(), anyString(), any(), any(), anyString(), anyString()))
                .willReturn(taskPage);
        given(taskListMapper.pageToTaskPagedListResponseDto(taskPage))
                .willReturn(taskPagedListResponseDto);

        mockMvc.perform(
                        MockMvcRequestBuilders.get(TASKS_URL)
                                .param("page", "1")
                                .param("size", "10")
                )
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void givenInvalidParameters_whenGetAllTasks_thenReturnBadRequest() throws Exception {
        Page<Task> taskPage = TaskFactory.createTaskPage();
        TaskPagedListResponseDto taskPagedListResponseDto = TaskFactory.createTaskPagedListResponseDto();

        given(taskService.getAllTasks(anyInt(), anyInt(), anyString(), anyString(), any(), any(), anyString(), anyString()))
                .willReturn(taskPage);
        given(taskListMapper.pageToTaskPagedListResponseDto(taskPage))
                .willReturn(taskPagedListResponseDto);

        mockMvc.perform(
                        MockMvcRequestBuilders.get(TASKS_URL)
                                .param("page", "-1")
                                .param("size", "10")
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void givenValidParameters_whenGetTasksForUser_thenReturnOk() throws Exception {
        Page<Task> taskPage = TaskFactory.createTaskPage();
        TaskPagedListResponseDto taskPagedListResponseDto = TaskFactory.createTaskPagedListResponseDto();

        given(taskService.getTasksForUser(anyInt(), anyInt(), anyString(), anyString(), any(), any(), anyString(), anyString(), anyLong()))
                .willReturn(taskPage);
        given(taskListMapper.pageToTaskPagedListResponseDto(taskPage))
                .willReturn(taskPagedListResponseDto);

        mockMvc.perform(
                        MockMvcRequestBuilders.get(USER_TASKS_URL)
                                .param("page", "1")
                                .param("size", "10")
                )
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void givenInvalidParameters_whenGetTasksForUser_thenReturnBadRequest() throws Exception {
        Page<Task> taskPage = TaskFactory.createTaskPage();
        TaskPagedListResponseDto taskPagedListResponseDto = TaskFactory.createTaskPagedListResponseDto();

        given(taskService.getTasksForUser(anyInt(), anyInt(), anyString(), anyString(), any(), any(), anyString(), anyString(), anyLong()))
                .willReturn(taskPage);
        given(taskListMapper.pageToTaskPagedListResponseDto(taskPage))
                .willReturn(taskPagedListResponseDto);

        mockMvc.perform(
                        MockMvcRequestBuilders.get(USER_TASKS_URL)
                                .param("page", "1")
                                .param("size", "10")
                                .param("priority", "invalid")
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void givenValidParameters_whenGetTasksForOwner_thenReturnOk() throws Exception {
        Page<Task> taskPage = TaskFactory.createTaskPage();
        TaskPagedListResponseDto taskPagedListResponseDto = TaskFactory.createTaskPagedListResponseDto();

        given(taskService.getTasksForOwner(anyInt(), anyInt(), anyString(), anyString(), any(), any(), anyString(), anyString(), anyLong()))
                .willReturn(taskPage);
        given(taskListMapper.pageToTaskPagedListResponseDto(taskPage))
                .willReturn(taskPagedListResponseDto);

        mockMvc.perform(
                        MockMvcRequestBuilders.get(OWNER_TASKS_URL)
                                .param("page", "1")
                                .param("size", "10")
                )
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void givenInvalidParameters_whenGetTasksForOwner_thenReturnBadRequest() throws Exception {
        Page<Task> taskPage = TaskFactory.createTaskPage();
        TaskPagedListResponseDto taskPagedListResponseDto = TaskFactory.createTaskPagedListResponseDto();

        given(taskService.getTasksForOwner(anyInt(), anyInt(), anyString(), anyString(), any(), any(), anyString(), anyString(), anyLong()))
                .willReturn(taskPage);
        given(taskListMapper.pageToTaskPagedListResponseDto(taskPage))
                .willReturn(taskPagedListResponseDto);

        mockMvc.perform(
                        MockMvcRequestBuilders.get(OWNER_TASKS_URL)
                                .param("page", "1")
                                .param("size", "10")
                                .param("priority", "invalid")
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void givenValidParameters_whenGetTasksForAssignee_thenReturnOk() throws Exception {
        Page<Task> taskPage = TaskFactory.createTaskPage();
        TaskPagedListResponseDto taskPagedListResponseDto = TaskFactory.createTaskPagedListResponseDto();

        given(taskService.getTasksForAssignee(anyInt(), anyInt(), anyString(), anyString(), any(), any(), anyString(), anyString(), anyLong()))
                .willReturn(taskPage);
        given(taskListMapper.pageToTaskPagedListResponseDto(taskPage))
                .willReturn(taskPagedListResponseDto);

        mockMvc.perform(
                        MockMvcRequestBuilders.get(ASSIGNEE_TASKS_URL)
                                .param("page", "1")
                                .param("size", "10")
                )
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void givenInvalidParameters_whenGetTasksForAssignee_thenReturnBadRequest() throws Exception {
        Page<Task> taskPage = TaskFactory.createTaskPage();
        TaskPagedListResponseDto taskPagedListResponseDto = TaskFactory.createTaskPagedListResponseDto();

        given(taskService.getTasksForAssignee(anyInt(), anyInt(), anyString(), anyString(), any(), any(), anyString(), anyString(), anyLong()))
                .willReturn(taskPage);
        given(taskListMapper.pageToTaskPagedListResponseDto(taskPage))
                .willReturn(taskPagedListResponseDto);

        mockMvc.perform(
                        MockMvcRequestBuilders.get(ASSIGNEE_TASKS_URL)
                                .param("page", "1")
                                .param("size", "10")
                                .param("priority", "invalid")
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void givenValidTaskId_whenGetTaskById_thenReturnOk() throws Exception {
        Task task = TaskFactory.createTask();
        TaskResponseDtoNoComments taskResponseDto = TaskFactory.createTaskResponseDtoNoComments();

        given(taskService.getTaskById(anyLong()))
                .willReturn(task);
        given(taskMapper.mapTaskToResponseDtoNoComments(task))
                .willReturn(taskResponseDto);

        mockMvc.perform(
                        MockMvcRequestBuilders.get(TASK_URL)
                )
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void givenInvalidTaskId_whenGetTaskById_thenReturnNotFound() throws Exception {
        Task task = TaskFactory.createTask();
        TaskResponseDtoNoComments taskResponseDto = TaskFactory.createTaskResponseDtoNoComments();

        given(taskService.getTaskById(anyLong()))
                .willThrow(TaskNotFoundException.class);
        given(taskMapper.mapTaskToResponseDtoNoComments(task))
                .willReturn(taskResponseDto);

        mockMvc.perform(
                        MockMvcRequestBuilders.get(TASK_URL)
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void givenInvalidTaskId_whenDeleteTaskById_thenReturnNotFound() throws Exception {
        given(taskService.getTaskById(anyLong()))
                .willThrow(TaskNotFoundException.class);

        mockMvc.perform(
                        MockMvcRequestBuilders.delete(TASK_URL)
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}