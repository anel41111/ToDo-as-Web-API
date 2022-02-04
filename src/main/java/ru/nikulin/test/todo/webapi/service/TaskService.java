package ru.nikulin.test.todo.webapi.service;

import ru.nikulin.test.todo.webapi.dto.TaskDto;
import ru.nikulin.test.todo.webapi.model.Task;

import java.util.List;

public interface TaskService {
    List<Task> getTasksByProjectId(Long projectId);
    boolean saveTaskForProject(TaskDto taskDto, Long projectId);
    boolean deleteTaskById(Long taskId);
    boolean updateTask(TaskDto taskDto);
}
