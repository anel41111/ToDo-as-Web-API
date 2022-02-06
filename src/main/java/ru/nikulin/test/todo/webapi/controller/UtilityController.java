package ru.nikulin.test.todo.webapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
public class UtilityController {

    @Operation(tags = {"Utility"},
            summary = "Checks whether the server is online")
    @GetMapping("/health_check")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Ok");
    }
}
