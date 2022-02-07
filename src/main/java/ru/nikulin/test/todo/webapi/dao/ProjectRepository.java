package ru.nikulin.test.todo.webapi.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nikulin.test.todo.webapi.model.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
}
