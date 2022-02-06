package ru.nikulin.test.todo.webapi.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import ru.nikulin.test.todo.webapi.dao.ProjectRepository;
import ru.nikulin.test.todo.webapi.dao.TaskRepository;
import ru.nikulin.test.todo.webapi.dto.ProjectDto;
import ru.nikulin.test.todo.webapi.model.Project;
import ru.nikulin.test.todo.webapi.model.Task;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test") //uses application-test.properties for enviroment
public class ViewEndpointTests {

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

    /**
     * Tests on a large dataset (100 projects, 1000 tasks)
     */
    @Nested
    public class LargeDataSetTests {

        @BeforeEach
        public void setUp() {
            InputStream is = ViewEndpointTests.class.getResourceAsStream("/data.sql");
            assertNotNull(is);
            String data = new BufferedReader(
                    new InputStreamReader(is, StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));
            assertDoesNotThrow(() -> jdbcTemplate.update(data, Collections.emptyMap()));
        }

        @ParameterizedTest
        @ValueSource(ints = {1, 20, 15, 8, 9})
        public void shouldReturnCorrectAmountOfResults(int pageSize) {
            var result = projectService.findAllProjects(0, pageSize, "id");
            assertNotNull(result);
            assertEquals(pageSize, result.size());
            assertInstanceOf(ProjectDto.class, result.get(0));
        }

        @Test
        public void shouldNotContainAnInstanceOnAnotherPage() {
            int TEST_PAGE_SIZE = 10;
            var pageResult = projectService.findAllProjects(0, TEST_PAGE_SIZE, "id");
            assertNotNull(pageResult);
            assertEquals(TEST_PAGE_SIZE, pageResult.size());
            var result = pageResult.get(0);
            var nextPageResult = projectService.findAllProjects(1, TEST_PAGE_SIZE, "id");
            assertNotNull(nextPageResult);
            assertEquals(TEST_PAGE_SIZE, nextPageResult.size());
            assertFalse(nextPageResult.contains(result));
        }

        @Test
        public void shouldThrowBecauseOfIncorrectArguments() {
            assertThrows(IllegalArgumentException.class, () -> projectService.findAllProjects(-1, 0, "id"));
            assertThrows(IllegalArgumentException.class, () -> projectService.findAllProjects(0, 1, null));
            assertThrows(IllegalArgumentException.class, () -> projectService.findAllProjects(0, Integer.MIN_VALUE, "id"));
        }

        @ParameterizedTest
        @ValueSource(longs = {1, 12, 46, 55, 23})
        public void shouldReturnProject(long id) {
            var projectContainer = projectService.findProjectById(id);
            assertTrue(projectContainer.isPresent());
            var project = projectContainer.get();
            assertNotNull(project);
            assertNotNull(project.getId());
            assertNotNull(project.getProjectName());
            assertNotNull(project.getPriority());
            assertTrue(project.getPriority().compareTo(0) > 0 && project.getPriority().compareTo(100) <= 0);
            assertNotNull(project.getProjectStatus());
            assertNotNull(project.getProjectStartDate());
            assertNotNull(project.getTasks());
        }

        @Test
        public void shouldReturnEmptyOptional() {
            assertTrue(projectService.findProjectById(Long.MAX_VALUE).isEmpty());
            assertTrue(projectService.findProjectById(101L).isEmpty());
        }

        @Test
        public void shouldThrowOnIncorrectArg() {
            assertThrows(IllegalArgumentException.class, () -> projectService.findProjectById(-1L));
            assertThrows(IllegalArgumentException.class, () -> projectService.findProjectById(Long.MIN_VALUE));
            assertThrows(IllegalArgumentException.class, () -> projectService.findProjectById(null));
            assertThrows(IllegalArgumentException.class, () -> projectService.findProjectById(0L));
        }

        @ParameterizedTest
        @ValueSource(longs = {1, 12, 46, 55, 23})
        public void shouldMatchTaskAmount(long id) {
            var container = projectService.findProjectById(id);
            assertTrue(container.isPresent());
            var expectedTasks = container.get().getTasks();
            var resultTasks = taskService.getTasksByProjectId(id);
            assertTrue(resultTasks.containsAll(expectedTasks));
            assertEquals(expectedTasks.size(), resultTasks.size());
        }


        @ParameterizedTest
        @ValueSource(longs = {1, 120, 466, 551, 231})
        public void shouldReturnTask(long id) {
            var taskContainer = taskService.findTaskById(id);
            assertTrue(taskContainer.isPresent());
            var task = taskContainer.get();
            assertNotNull(task);
            assertNotNull(task.getId());
            assertNotNull(task.getDescription());
            assertNotNull(task.getTaskName());
            assertNotNull(task.getTaskStatus());
            assertNotNull(task.getPriority());
            assertTrue(task.getPriority().compareTo(0) > 0 && task.getPriority().compareTo(100) <= 0);
        }

        @Test
        public void shouldReturnEmptyTaskOptional() {
            assertTrue(taskService.findTaskById(Long.MAX_VALUE).isEmpty());
            assertTrue(taskService.findTaskById(1001L).isEmpty());
        }


        @Test
        public void shouldThrowOnIncorrectArgWhenFindingTask() {
            assertThrows(IllegalArgumentException.class, () -> taskService.findTaskById(-1L));
            assertThrows(IllegalArgumentException.class, () -> taskService.findTaskById(Long.MIN_VALUE));
            assertThrows(IllegalArgumentException.class, () -> taskService.findTaskById(null));
            assertThrows(IllegalArgumentException.class, () -> taskService.findTaskById(0L));
        }
    }

    /**
     * Tests with a small dataset (1 project, 1 task by default)
     */
    @Nested
    public class SmallDataSetTests {

        Task TEST_TASK;
        Project TEST_PROJECT;

        @BeforeEach
        public void setUp() {
            TEST_TASK = taskRepository.save(new Task(
                    null,
                    "TEST_TASK",
                    "TEST_TASK_DESCRIPTION",
                    100, null,
                    Task.TaskStatus.ToDo));

            TEST_PROJECT = projectRepository.save(new Project(null,
                    "TEST_PROJECT",
                    LocalDateTime.now(),
                    null,
                    Project.ProjectStatus.NotStarted,
                    10,
                    List.of(TEST_TASK)));
        }

        @Test
        public void shouldGetEqualObjects() {
            var resultContainer = projectService.findProjectById(TEST_PROJECT.getId());
            assertTrue(resultContainer.isPresent());
            var result = resultContainer.get();

            assertEquals(TEST_PROJECT.getId(), result.getId());
            assertEquals(TEST_PROJECT.getProjectName(), result.getProjectName());
            assertEquals(TEST_PROJECT.getProjectStatus().toString(), result.getProjectStatus().toString());
//            assertTrue(TEST_PROJECT.getProjectStartDate().compareTo(result.getProjectStartDate()) <= 0);
            assertEquals(TEST_PROJECT.getProjectCompletionDate(), result.getProjectCompletionDate());
            assertEquals(TEST_PROJECT.getTasks().size(), result.getTasks().size());
            var resultTask = result.getTasks().get(0);

            assertEquals(TEST_TASK.getTaskName(), resultTask.getTaskName());
            assertEquals(TEST_TASK.getTaskStatus().toString(), resultTask.getTaskStatus().toString());
            assertEquals(TEST_TASK.getDescription(), resultTask.getDescription());
            assertEquals(TEST_TASK.getPriority(), resultTask.getPriority());
        }

        @Test
        public void shouldReturnNothing() {
            assertTrue(projectService.findProjectById(999L).isEmpty());
            assertTrue(taskService.findTaskById(9999L).isEmpty());
        }

    }

}
