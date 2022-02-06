package ru.nikulin.test.todo.webapi.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.nikulin.test.todo.webapi.dao.ProjectRepository;
import ru.nikulin.test.todo.webapi.dto.ProjectDto;
import ru.nikulin.test.todo.webapi.dto.ProjectStatusDto;
import ru.nikulin.test.todo.webapi.service.ProjectService;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
        return projectRepository.findById(id).map(s -> mapper.map(s, ProjectDto.class));
    }

    @Override
    public boolean addProject(ProjectDto projectDto) {
        return false;
    }

    @Override
    public boolean deleteProject(ProjectDto projectDto) {
        return false;
    }

    @Override
    public boolean updateProject(ProjectDto projectDto) {
        return false;
    }
}
