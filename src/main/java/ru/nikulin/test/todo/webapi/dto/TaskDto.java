package ru.nikulin.test.todo.webapi.dto;

import lombok.*;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(of = "id")
public class TaskDto {
    @Null(message = "Do not specify id of a new task, it will be generated automatically")
    private Long id;

    @NotBlank(message = "Task name must not be blank or empty")
    private String taskName;

    @Nullable
    private String description;

    @NotNull(message = "Task priority must be specified")
    private Integer priority;

    @NotNull(message = "Task status must be specified")
    private TaskStatusDto taskStatus;
}
