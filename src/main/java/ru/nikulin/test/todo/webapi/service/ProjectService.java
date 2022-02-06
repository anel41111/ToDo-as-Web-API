package ru.nikulin.test.todo.webapi.service;

import ru.nikulin.test.todo.webapi.dto.ProjectDto;
import ru.nikulin.test.todo.webapi.dto.ProjectStatusDto;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProjectService {
    List<ProjectDto> findAllProjects(Integer pageNo, Integer pageSize, String sortBy);

    Map<ProjectStatusDto, ProjectDto> findAllProjectsGroupedByStatus();

    Optional<ProjectDto> findProjectById(Long id);

    ProjectDto addProject(ProjectDto projectDto);

    void deleteProject(Long projectId);

    ProjectDto updateProject(ProjectDto projectDto, Long projectId);
}
