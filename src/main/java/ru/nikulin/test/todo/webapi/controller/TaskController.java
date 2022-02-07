package ru.nikulin.test.todo.webapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.nikulin.test.todo.webapi.dto.TaskDto;
import ru.nikulin.test.todo.webapi.service.TaskService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Controller
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @Operation(
            tags = {"Tasks"},
            summary = "Get task details"
    )
    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getTask(
            @Valid
            @Min(value = 1L, message = "Id cannot be less than 1")
            @Parameter(name = "id", description = "id of a task", required = true)
            @PathVariable Long id) {
        var task = taskService.findTaskById(id).orElse(null);
        return ResponseEntity.ok(task);
    }

    @Operation(
            tags = {"Tasks"},
            summary = "Update a task by id"
    )
    @PutMapping("/{id}")
    public ResponseEntity<TaskDto> updateTask(
            @Valid
            @Min(value = 1L, message = "Id cannot be less than 1")
            @Parameter(name = "id", description = "id of a task", required = true)
            @PathVariable Long id,
            @Valid
            @NotNull(message = "Task cannot be null, if you wish to delete project at this id, use delete mapping.")
            @Parameter(name = "task", description = "body of a task", required = true)
            @RequestBody TaskDto task) {
        var result = taskService.updateTask(task, id);
        return ResponseEntity.ok(result);
    }

    @Operation(
            tags = {"Tasks"},
            summary = "Delete a task by id"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(
            @Valid
            @Min(value = 1L, message = "Id cannot be less than 1")
            @Parameter(name = "id", description = "id of a task", required = true)
            @PathVariable Long id) {
        taskService.deleteTaskById(id);
        return ResponseEntity.ok("");
    }

}
