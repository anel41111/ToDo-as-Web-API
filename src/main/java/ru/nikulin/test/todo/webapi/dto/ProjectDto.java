package ru.nikulin.test.todo.webapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ProjectDto {
    private Long id;
    private String projectName;
    private LocalDateTime projectStartDate;
    private LocalDateTime projectCompletionDate;
    private ProjectStatusDto projectStatus;
    private Integer priority;
    private List<TaskDto> tasks;
}
