package ru.nikulin.test.todo.webapi.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.nikulin.test.todo.webapi.model.Task;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query(value = "SELECT * FROM tasks " +
            "WHERE id in " +
            "(SELECT tasks_id from projects_tasks " +
            "WHERE project_id = :projectId)", nativeQuery = true)
    List<Task> findAllByProjectId(Long projectId);
}
