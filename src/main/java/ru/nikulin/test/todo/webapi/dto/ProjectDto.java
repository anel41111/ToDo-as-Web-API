package ru.nikulin.test.todo.webapi.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(of = "id")
public class ProjectDto {

    @Min(1L)
    @Nullable
    private Long id;

    @NotBlank(message = "Name of a project must not be blank or empty")
    private String projectName;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime projectStartDate;

    @Nullable
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime projectCompletionDate;

    @NotNull(message = "Project status must be specified")
    private ProjectStatusDto projectStatus;

    @Min(0L)
    @Max(100L)
    @NotNull(message = "Project priority must be specified")
    private Integer priority;

    @NotNull
    private List<TaskDto> tasks;
}
