package ru.nikulin.test.todo.webapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.nikulin.test.todo.webapi.dto.ProjectDto;
import ru.nikulin.test.todo.webapi.dto.TaskDto;
import ru.nikulin.test.todo.webapi.service.ProjectService;
import ru.nikulin.test.todo.webapi.service.TaskService;

import java.util.List;

@Controller
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final TaskService taskService;

    @Operation(tags = {"Projects"},
            summary = "Get a list of all projects"
    )
    @GetMapping("")
    public ResponseEntity<Object> getProjects(
            @Parameter(name = "pageNo",
                    description = "Used for pagination, number of pages to skip.")
            @RequestParam(defaultValue = "0") Integer pageNo,
            @Parameter(name = "pageSize", description = "Used for pagination, number of results per page.")
            @RequestParam(defaultValue = "3") Integer pageSize,
            @Parameter(name = "sortBy", description = "Specify a field by which to sort.",
                    schema = @Schema(type = "string", defaultValue = "id",
                            allowableValues = {"id", "priority", "projectStatus", "projectStartDate", "projectCompletionDate"}))
            @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(name = "groupByStatus", description = "Specify whether to group result by status of the project",
                    schema = @Schema(type = "boolean", defaultValue = "false"))
//                            allowableValues = {"id", "priority", "projectStatus", "projectStartDate", "projectCompletionDate"}))
            @RequestParam(defaultValue = "false") Boolean groupByStatus) {
        var projectList = projectService.findAllProjects(pageNo, pageSize, sortBy);
        /*if (groupByStatus) { //TODO: implement
            var projectMap = projectList.stream()
                    .collect(Collectors.groupingBy());
            return ResponseEntity.ok(projectMap);
        }*/
        return ResponseEntity.ok(projectList);
    }

    @Operation(tags = {"Projects"},
            summary = "Get project details"
    )
    @GetMapping("/{id}")
    public ResponseEntity<ProjectDto> getProjectById(
            @Parameter(name = "id", description = "id of a project", required = true)
            @PathVariable Long id) {
        var project = projectService.findProjectById(id);
        return ResponseEntity.ok(project);
    }

    @Operation(tags = {"Projects"},
            summary = "Get tasks related to a project"
    )
    @GetMapping("/{id}/tasks")
    public ResponseEntity<List<TaskDto>> getProjectTasks(
            @Parameter(name = "id", description = "id of a project", required = true)
            @PathVariable Long id,
            @Parameter(name = "pageNo",
                    description = "Used for pagination, number of pages to skip.")
            @RequestParam(defaultValue = "0") Integer pageNo,
            @Parameter(name = "pageSize", description = "Used for pagination, number of results per page.")
            @RequestParam(defaultValue = "3") Integer pageSize,
            @Parameter(name = "sortBy", description = "Specify a field by which to sort.",
                    schema = @Schema(type = "string", defaultValue = "id",
                            allowableValues = {"id", "priority", "taskStatus"}))
            @RequestParam(defaultValue = "id") String sortBy) {
        var taskList = taskService.getTasksByProjectId(id, pageNo, pageSize, sortBy);
        return ResponseEntity.ok(taskList);
    }
}
