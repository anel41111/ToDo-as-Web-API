package ru.nikulin.test.todo.webapi.service;

import ru.nikulin.test.todo.webapi.dto.ProjectDto;
import ru.nikulin.test.todo.webapi.dto.ProjectStatusDto;

import java.util.List;
import java.util.Map;

public interface ProjectService {
    List<ProjectDto> findAllProjects(Integer pageNo, Integer pageSize, String sortBy);

    Map<ProjectStatusDto, ProjectDto> findAllProjectsGroupedByStatus();

    ProjectDto findProjectById(Long id);

    boolean addProject(ProjectDto projectDto);

    boolean deleteProject(ProjectDto projectDto);

    boolean updateProject(ProjectDto projectDto);
}
