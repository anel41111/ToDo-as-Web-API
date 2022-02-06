package ru.nikulin.test.todo.webapi.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import ru.nikulin.test.todo.webapi.dao.ProjectRepository;
import ru.nikulin.test.todo.webapi.dao.TaskRepository;
import ru.nikulin.test.todo.webapi.dto.ProjectDto;
import ru.nikulin.test.todo.webapi.dto.ProjectStatusDto;
import ru.nikulin.test.todo.webapi.dto.TaskDto;
import ru.nikulin.test.todo.webapi.dto.TaskStatusDto;
import ru.nikulin.test.todo.webapi.exception.TaskManagerExtension;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test") //uses application-test.properties for enviroment
public class MuteEndpointTests {

    ProjectDto TEST_PROJECT_DTO = new ProjectDto(
            null,
            "TEST_PROJECT_NAME",
            LocalDateTime.now(),
            null,
            ProjectStatusDto.Active,
            100,
            null);
    TaskDto TEST_TASK_DTO = new TaskDto(
            null,
            "TEST_TASK_NAME",
            "TEST_TASK_DESCRIPTION",
            100,
            TaskStatusDto.ToDo,
            null);

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskRepository taskRepository;

    @AfterEach
    public void tearDown() {
        jdbcTemplate.update("TRUNCATE TABLE projects, tasks, projects_tasks RESTART IDENTITY", Collections.emptyMap());
    }

    @Nested
    public class PostEndpointTests {

        @Test
        public void shouldSuccessfullyInsertProjectWithNoTasks() {
            var result = projectService.addProject(TEST_PROJECT_DTO);
            assertNotNull(result);
            assertNotNull(result.getId());
            assertEquals(TEST_PROJECT_DTO.getProjectName(), result.getProjectName());
            assertEquals(TEST_PROJECT_DTO.getPriority(), result.getPriority());
            assertEquals(TEST_PROJECT_DTO.getProjectStatus(), result.getProjectStatus());
            assertTrue(TEST_PROJECT_DTO.getProjectStartDate().compareTo(result.getProjectStartDate()) <= 0);
            assertEquals(TEST_PROJECT_DTO.getProjectCompletionDate(), result.getProjectCompletionDate());
            assertEquals(TEST_PROJECT_DTO.getTasks(), result.getTasks());
        }

        @Test
        public void shouldSuccessfullyAddTaskForProjectId() {
            var projectId = projectService.addProject(TEST_PROJECT_DTO).getId();
            assertNotNull(projectId);
            var newTask = taskService.addTaskForProject(TEST_TASK_DTO, projectId);
            assertNotNull(newTask);
            assertNotNull(newTask.getId());
            assertEquals(TEST_TASK_DTO.getTaskName(), newTask.getTaskName());
            assertEquals(TEST_TASK_DTO.getDescription(), newTask.getDescription());
            assertEquals(TEST_TASK_DTO.getPriority(), newTask.getPriority());
            assertEquals(TEST_TASK_DTO.getTaskStatus(), newTask.getTaskStatus());
        }
    }

    @Nested
    public class PutEndpointTests {

        String TEST_NEW_PROJECT_NAME = "TEST_NEW_PROJECT_NAME";

        LocalDateTime TEST_NEW_COMPLETION_DATE = LocalDateTime.now().plus(2, ChronoUnit.DAYS);

        ProjectStatusDto TEST_NEW_STATUS = ProjectStatusDto.Completed;

        String TEST_NEW_TASK_NAME = "TEST_NEW_TASK_NAME";

        String TEST_NEW_TASK_DESCRIPTION = "TEST_NEW_TASK_DESCRIPTION";

        TaskStatusDto TEST_NEW_TASK_STATUS = TaskStatusDto.Done;

        @Test
        public void shouldSuccessfullyUpdateSingleProject() {
            var result = projectService.addProject(TEST_PROJECT_DTO);
            assertNotNull(result);
            assertNull(result.getProjectCompletionDate());
            result.setProjectCompletionDate(TEST_NEW_COMPLETION_DATE);
            assertNotEquals(TEST_NEW_PROJECT_NAME, result.getProjectName());
            result.setProjectName(TEST_NEW_PROJECT_NAME);
            assertNotEquals(TEST_NEW_STATUS, result.getProjectStatus());
            result.setProjectStatus(TEST_NEW_STATUS);
            var newResult = projectService.updateProject(result, result.getId());
            assertNotNull(newResult);
            assertNotNull(result.getProjectCompletionDate());
            assertEquals(TEST_NEW_STATUS, result.getProjectStatus());
        }

        @Test
        public void shouldFailUpdateOfNonExistingSingleProject() {
            var result = TEST_PROJECT_DTO;
            result.setId(100L);
            assertThrows(TaskManagerExtension.class, () -> projectService.updateProject(result, result.getId()));
        }

        @Test
        public void shouldFailUpdateOfNonExistingTask() {
            var result = TEST_TASK_DTO;
            result.setId(100L);
            assertThrows(TaskManagerExtension.class, () -> taskService.updateTask(result, result.getId()));
        }

        @Test
        public void shouldSuccessfullyUpdateSingleTask() {
            var projectId = projectService.addProject(TEST_PROJECT_DTO).getId();
            assertNotNull(projectId);
            var newTask = taskService.addTaskForProject(TEST_TASK_DTO, projectId);
            assertNotNull(newTask);
            assertNotEquals(TEST_NEW_TASK_NAME, newTask.getTaskName());
            newTask.setTaskName(TEST_NEW_TASK_NAME);
            assertNotEquals(TEST_NEW_TASK_DESCRIPTION, newTask.getDescription());
            newTask.setDescription(TEST_NEW_TASK_DESCRIPTION);
            assertNotEquals(TEST_NEW_TASK_STATUS, newTask.getTaskStatus());
            newTask.setTaskStatus(TEST_NEW_TASK_STATUS);

            var result = taskService.updateTask(newTask, projectId);
            assertEquals(TEST_NEW_TASK_NAME, result.getTaskName());
            assertEquals(TEST_NEW_TASK_DESCRIPTION, result.getDescription());
            assertEquals(TEST_NEW_TASK_STATUS, result.getTaskStatus());
        }
    }
}
