package ru.nikulin.test.todo.webapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import ru.nikulin.test.todo.webapi.dto.ProjectDto;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
//@EnabledIfSystemProperties() /*TODO: set active spring property*/
public class ProjectServiceTest {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private TaskService taskService;

    @BeforeEach
    /*Populate test tables with mock data from sql script*/
    public void setUp() {
        InputStream is = ProjectServiceTest.class.getResourceAsStream("/data.sql");
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
        var resultTasks = taskService.getTasksByProjectId(id, 0, expectedTasks.size(), "id");
        assertTrue(resultTasks.containsAll(expectedTasks));
        assertEquals(expectedTasks.size(), resultTasks.size());
    }
}
