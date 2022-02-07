package ru.nikulin.test.todo.webapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.nikulin.test.todo.webapi.dto.ProjectDto;
import ru.nikulin.test.todo.webapi.dto.TaskDto;
import ru.nikulin.test.todo.webapi.service.ProjectService;
import ru.nikulin.test.todo.webapi.service.TaskService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
            @Valid
            @Min(value = 0L, message = "Id cannot be less than 1")
            @Parameter(name = "pageNo",
                    description = "Used for pagination, number of pages to skip.")
            @RequestParam(defaultValue = "0") Integer pageNo,
            @Valid
            @Min(value = 1L, message = "Size of page cannot be less than 1")
            @Parameter(name = "pageSize", description = "Used for pagination, number of results per page.")
            @RequestParam(defaultValue = "3") Integer pageSize,
            @Valid
            @NotBlank(message = "Cannot be blank, set 'id' for default behavior")
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
            @Valid
            @Min(value = 1L, message = "Id cannot be less than 1")
            @Parameter(name = "id", description = "id of a project", required = true)
            @PathVariable Long id) {
        var project = projectService.findProjectById(id).orElse(null);
        return ResponseEntity.ok(project);
    }

    @Operation(tags = {"Tasks"},
            summary = "Get tasks related to a project"
    )
    @GetMapping("/{id}/tasks")
    public ResponseEntity<List<TaskDto>> getProjectTasks(
            @Valid
            @Min(value = 1L, message = "Id cannot be less than 1")
            @Parameter(name = "id", description = "id of a project", required = true)
            @PathVariable Long id) {
        var taskList = taskService.getTasksByProjectId(id);
        return ResponseEntity.ok(taskList);
    }

    @Operation(tags = {"Projects"},
            summary = "Update a project by id"
    )
    @PutMapping("/{id}")
    public ResponseEntity<ProjectDto> updateProjectById(
            @Valid
            @Min(value = 1L, message = "Id cannot be less than 1")
            @Parameter(name = "id", description = "id of a project", required = true)
            @PathVariable Long id,
            @Valid
            @NotNull(message = "Project cannot be null, if you wish to delete project at this id, use delete mapping.")
            @Parameter(name = "project", description = "Body of a project", required = true)
            @RequestBody ProjectDto project) {
        var newProject = projectService.updateProject(project, id);
        return ResponseEntity.ok(newProject);
    }

    @Operation(tags = {"Projects"},
            summary = "Delete a project by id"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProjectById(
            @Valid
            @Min(value = 1L, message = "Id cannot be less than 1")
            @Parameter(name = "id", description = "id of a project", required = true)
            @PathVariable Long id
    ) {
        projectService.deleteProject(id);
        return ResponseEntity.ok("");
    }


    @Operation(tags = {"Projects"},
            summary = "Add new projects"
    )
    @PostMapping("")
    public ResponseEntity<List<ProjectDto>> addProjects(
            @Valid
            @NotEmpty(message = "Array of projects cannot be empty, at least 1 entity is required.")
            @Parameter(name = "projects", description = "Array of projects", required = true)
            @RequestBody ProjectDto[] projects) {
        List<ProjectDto> listResult;

        if (projects.length > 1) {
            listResult = projectService.addProjects(projects);
        } else {
            listResult = List.of(projectService.addProject(projects[0]));
        }
        return ResponseEntity.ok(listResult);
    }

    @Operation(tags = {"Tasks"},
            summary = "Add new tasks"
    )
    @PostMapping("/{id}/tasks")
    public ResponseEntity<List<TaskDto>> addTasks(
            @Valid
            @Min(value = 1L, message = "Id cannot be less than 1")
            @Parameter(name = "id", description = "id of a project", required = true)
            @PathVariable Long id,
            @Valid
            @NotEmpty(message = "Array of tasks cannot be empty, at least 1 entity is required.")
            @Parameter(name = "tasks", description = "Array of tasks", required = true)
            @RequestBody TaskDto[] tasks) {
        List<TaskDto> listResult;

        if (tasks.length > 1) {
            listResult = taskService.addTasksForProject(tasks, id);
        } else {
            listResult = List.of(taskService.addTaskForProject(tasks[0], id));
        }
        return ResponseEntity.ok(listResult);
    }
}
