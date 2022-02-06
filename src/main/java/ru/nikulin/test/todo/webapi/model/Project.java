package ru.nikulin.test.todo.webapi.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "projects")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String projectName;

    @Column
    private LocalDateTime projectStartDate;

    @Column
    private LocalDateTime projectCompletionDate;

    @Column
    private ProjectStatus projectStatus;

    @Column
    private Integer priority;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @OrderBy("id")
    private List<Task> tasks;

    public enum ProjectStatus {
        NotStarted, Active, Completed
    }
}
