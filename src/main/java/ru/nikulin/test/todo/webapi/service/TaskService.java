package ru.nikulin.test.todo.webapi.service;

import ru.nikulin.test.todo.webapi.dto.TaskDto;

import java.util.List;
import java.util.Optional;

public interface TaskService {
    List<TaskDto> getTasksByProjectId(Long projectId);

    boolean saveTaskForProject(TaskDto taskDto, Long projectId);

    boolean deleteTaskById(Long taskId);

    boolean updateTask(TaskDto taskDto);

    Optional<TaskDto> findTaskById(Long id);
}
