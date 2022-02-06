package ru.nikulin.test.todo.webapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(of = "id")
public class ProjectDto {

    @Null(message = "Do not specify id of a new project, it will be generated automatically")
    private Long id;

    @NotBlank(message = "Name of a project must not be blank or empty")
    private String projectName;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="Europe/Moscow")
    private LocalDateTime projectStartDate;

    @Nullable
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="Europe/Moscow")
    private LocalDateTime projectCompletionDate;

    @NotNull(message = "Project status must be specified")
    private ProjectStatusDto projectStatus;

    @NotNull(message = "Project priority must be specified")
    private Integer priority;

    @NotNull
    private List<TaskDto> tasks;
}
