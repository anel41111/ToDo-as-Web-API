package ru.nikulin.test.todo.webapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class TaskDto {
    Long id;
    String taskName;
    String description;
    private Integer priority;
    private TaskStatusDto taskStatus;
}
