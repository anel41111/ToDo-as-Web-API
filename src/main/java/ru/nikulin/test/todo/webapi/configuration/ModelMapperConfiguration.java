package ru.nikulin.test.todo.webapi.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.nikulin.test.todo.webapi.dto.ProjectStatusDto;
import ru.nikulin.test.todo.webapi.dto.TaskDto;
import ru.nikulin.test.todo.webapi.dto.TaskStatusDto;
import ru.nikulin.test.todo.webapi.model.Project;
import ru.nikulin.test.todo.webapi.model.Task;

@Configuration
public class ModelMapperConfiguration {

    /**
     * Configuration of a model Mapper bean
     */
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.typeMap(Task.TaskStatus.class, TaskStatusDto.class).setConverter(context -> {
            switch (context.getSource()) {
                case Done:
                    return TaskStatusDto.Done;
                case ToDo:
                    return TaskStatusDto.ToDo;
                case InProgress:
                    return TaskStatusDto.InProgress;
            }
            return null;
        });
        modelMapper.typeMap(Project.ProjectStatus.class, ProjectStatusDto.class).setConverter(context -> {
            switch (context.getSource()) {
                case Active:
                    return ProjectStatusDto.Active;
                case Completed:
                    return ProjectStatusDto.Completed;
                case NotStarted:
                    return ProjectStatusDto.NotStarted;
            }
            return null;
        });
        modelMapper.typeMap(Task.class, TaskDto.class).addMapping(source -> source.getProject().getId(), (destination, value) -> destination.setProjectId((Long) value));
        return modelMapper;
    }
}
