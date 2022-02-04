package ru.nikulin.test.todo.webapi.service;

import ru.nikulin.test.todo.webapi.dto.TaskDto;

import java.util.List;

public interface TaskService {
    List<TaskDto> getTasksByProjectId(Long projectId, Integer pageNo, Integer pageSize, String sortBy);

    boolean saveTaskForProject(TaskDto taskDto, Long projectId);

    boolean deleteTaskById(Long taskId);

    boolean updateTask(TaskDto taskDto);

    TaskDto getTask(Long taskId);
}
