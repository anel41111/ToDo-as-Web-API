package ru.nikulin.test.todo.webapi.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.nikulin.test.todo.webapi.dao.ProjectRepository;
import ru.nikulin.test.todo.webapi.dto.ProjectDto;
import ru.nikulin.test.todo.webapi.dto.ProjectStatusDto;
import ru.nikulin.test.todo.webapi.service.ProjectService;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    @Override
    public List<ProjectDto> findAllProjects(Integer pageNo, Integer pageSize, String sortBy) {
        var pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        var result = projectRepository.findAll(pageable);
//        result.get()
        return null;
    }

    @Override
    public Map<ProjectStatusDto, ProjectDto> findAllProjectsGroupedByStatus() {
        return null;
    }

    @Override
    public ProjectDto findProjectById(Long id) {
        return null;
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
