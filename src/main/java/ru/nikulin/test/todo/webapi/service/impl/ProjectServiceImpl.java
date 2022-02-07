package ru.nikulin.test.todo.webapi.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.nikulin.test.todo.webapi.dao.ProjectRepository;
import ru.nikulin.test.todo.webapi.dto.ProjectDto;
import ru.nikulin.test.todo.webapi.dto.ProjectStatusDto;
import ru.nikulin.test.todo.webapi.exception.EntityDoesNotExistException;
import ru.nikulin.test.todo.webapi.model.Project;
import ru.nikulin.test.todo.webapi.service.ProjectService;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ModelMapper mapper;

    @Override
    public List<ProjectDto> findAllProjects(Integer pageNo, Integer pageSize, String sortBy) {
        var pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        var result = projectRepository.findAll(pageable);

        if (result.isEmpty()) {
            return Collections.emptyList();
        }
        return result.get().map(s -> mapper.map(s, ProjectDto.class)).collect(Collectors.toList());
    }

    @Override
    public Map<ProjectStatusDto, ProjectDto> findAllProjectsGroupedByStatus() {
        return null;
    }

    @Override
    public Optional<ProjectDto> findProjectById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Id cannot be null or <= 0");
        }
        if (!projectRepository.existsById(id)) {
            throw new EntityDoesNotExistException(String.format("Project with specified id %d does not exist!", id));
        }
        return projectRepository.findById(id).map(s -> mapper.map(s, ProjectDto.class));
    }

    @Override
    public ProjectDto addProject(ProjectDto projectDto) {
        projectDto.setId(null);
        var newProject = projectRepository.save(mapper.map(projectDto, Project.class));
        return mapper.map(newProject, ProjectDto.class);
    }

    @Override
    public List<ProjectDto> addProjects(ProjectDto[] projectDtos) {
        var newProject = projectRepository.saveAll(
                Arrays.stream(projectDtos)
                        .peek(s -> s.setId(null))
                        .map(s -> mapper.map(s, Project.class))
                        .collect(Collectors.toList()));

        return newProject.stream().map(s -> mapper.map(s, ProjectDto.class)).collect(Collectors.toList());
    }

    @Override
    public void deleteProject(Long projectId) {
        projectRepository.deleteById(projectId);
    }

    @Override
    public ProjectDto updateProject(ProjectDto projectDto, Long projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new EntityDoesNotExistException(String.format("Project with specified id %d does not exist!", projectId));
        }
        projectDto.setId(projectId);
        var newProject = projectRepository.save(mapper.map(projectDto, Project.class));
        return mapper.map(newProject, ProjectDto.class);
    }
}
