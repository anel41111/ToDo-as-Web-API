package ru.nikulin.test.todo.webapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.nikulin.test.todo.webapi.model.Task;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/api/tasks")
public class TaskController {

    @Operation(
            tags = {"Tasks"},
            summary = "Get all tasks for a project"
    )
    @GetMapping("")
    public ResponseEntity<List<Task>> tasks(@RequestParam Integer projectId) {
        return ResponseEntity.ok(Collections.emptyList());
    }
}
