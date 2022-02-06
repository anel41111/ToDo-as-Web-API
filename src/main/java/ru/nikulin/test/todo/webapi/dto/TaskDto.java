package ru.nikulin.test.todo.webapi.dto;

import lombok.*;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(of = "id")
public class TaskDto {
    @Min(1L)
    @Nullable
    private Long id;

    @NotBlank(message = "Task name must not be blank or empty")
    private String taskName;

    @Nullable
    private String description;

    @Min(0L)
    @Max(100L)
    @NotNull(message = "Task priority must be specified")
    private Integer priority;

    @NotNull(message = "Task status must be specified")
    private TaskStatusDto taskStatus;

    @Nullable
    private Long projectId;
}
