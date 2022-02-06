package ru.nikulin.test.todo.webapi.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.nikulin.test.todo.webapi.dao.ProjectRepository;
import ru.nikulin.test.todo.webapi.dao.TaskRepository;
import ru.nikulin.test.todo.webapi.dto.TaskDto;
import ru.nikulin.test.todo.webapi.exception.EntityDoesNotExistException;
import ru.nikulin.test.todo.webapi.model.Task;
import ru.nikulin.test.todo.webapi.service.TaskService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final ModelMapper mapper;
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    @Override
    public List<TaskDto> getTasksByProjectId(Long projectId) {
        var result = taskRepository.findAllByProjectId(projectId);

//        if (result.isEmpty()) {
//            return Collections.emptyList();
//        }
        return result.stream().map(s -> mapper.map(s, TaskDto.class)).collect(Collectors.toList());
    }

    @Override
    public TaskDto addTaskForProject(TaskDto taskDto, Long projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new EntityDoesNotExistException(String.format("Project with specified id %d does not exist!", projectId));
        }
        taskDto.setId(null);
        taskDto.setProjectId(projectId);
        var newTask = taskRepository.save(mapper.map(taskDto, Task.class));
        return mapper.map(newTask, TaskDto.class);
    }

    @Override
    public void deleteTaskById(Long taskId) {
        taskRepository.deleteById(taskId);
    }

    @Override
    public TaskDto updateTask(TaskDto taskDto, Long taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new EntityDoesNotExistException(String.format("Task with specified id %d does not exist!", taskId));
        }
        taskDto.setId(taskId);
        var newTask = taskRepository.save(mapper.map(taskDto, Task.class));
        return mapper.map(newTask, TaskDto.class);
    }


    @Override
    public Optional<TaskDto> findTaskById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Id cannot be null or <= 0");
        }
        return taskRepository.findById(id).map(s -> mapper.map(s, TaskDto.class));
    }
}
