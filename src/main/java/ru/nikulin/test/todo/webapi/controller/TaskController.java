package ru.nikulin.test.todo.webapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.nikulin.test.todo.webapi.dto.TaskDto;
import ru.nikulin.test.todo.webapi.service.TaskService;

@Controller
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @Operation(
            tags = {"Tasks"},
            summary = "Get task details"
    )
    @GetMapping("{id}")
    public ResponseEntity<TaskDto> getTask(
            @Parameter(name = "id", description = "id of a task", required = true)
            @PathVariable Long id) {
        var task = taskService.findTaskById(id).orElse(null);
        return ResponseEntity.ok(task);
    }
}
