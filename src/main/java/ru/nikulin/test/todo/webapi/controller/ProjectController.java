package ru.nikulin.test.todo.webapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.nikulin.test.todo.webapi.dto.ProjectDto;
import ru.nikulin.test.todo.webapi.service.ProjectService;

import java.util.List;

@Controller
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @Operation(tags = {"Projects"},
            summary = "Get a list of all projects"
    )
    @GetMapping
    @RequestMapping("")
    public ResponseEntity<List<ProjectDto>> getProjects(
            @Parameter(name = "pageNo",
                    description = "Used for pagination, number of pages to skip.")
            @RequestParam(defaultValue = "0") Integer pageNo,
            @Parameter(name = "pageSize", description = "Used for pagination, number of results per page.")
            @RequestParam(defaultValue = "3") Integer pageSize,
            @Parameter(name = "sortBy", description = "Specify a field by which to sort.",
                    schema = @Schema(type = "string", defaultValue = "id",
                            allowableValues = {"id", "priority", "projectStatus", "projectStartDate", "projectCompletionDate"}))
            @RequestParam(defaultValue = "id") String sortBy) {
        var projectList = projectService.findAllProjects(pageNo, pageSize, sortBy);
        return ResponseEntity.ok(projectList);
    }
}
