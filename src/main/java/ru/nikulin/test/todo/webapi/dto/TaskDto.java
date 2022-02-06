package ru.nikulin.test.todo.webapi.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(of = "id")
public class TaskDto {
    private Long id;
    private String taskName;
    private String description;
    private Integer priority;
    private TaskStatusDto taskStatus;
}
