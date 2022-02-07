package ru.nikulin.test.todo.webapi.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "tasks")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String taskName;

    @Column(columnDefinition = "text")
    private String description;

    @Column
    private Integer priority;

    @ManyToOne
    private Project project;

    @Column
    private TaskStatus taskStatus;

    public enum TaskStatus {
        ToDo, InProgress, Done
    }
}
