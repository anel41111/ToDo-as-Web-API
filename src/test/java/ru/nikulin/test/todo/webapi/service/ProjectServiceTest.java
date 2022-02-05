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
    private ProjectService service;

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
    public void shouldReturnCorrectAmount(int pageSize) {
        var result = service.findAllProjects(0, pageSize, "id");
        assertNotNull(result);
        assertEquals(pageSize, result.size());
        assertInstanceOf(ProjectDto.class, result.get(0));
    }

    @Test
    public void shouldThrowBecauseOfIncorrectArguments() {
        assertThrows(IllegalArgumentException.class, () -> service.findAllProjects(-1, 0, "id"));
        assertThrows(IllegalArgumentException.class, () -> service.findAllProjects(0, 1, null));
        assertThrows(IllegalArgumentException.class, () -> service.findAllProjects(0, Integer.MIN_VALUE, "id"));
    }

}
