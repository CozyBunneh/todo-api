package com.example.todo.infrastructure.persistence.entities;

import io.quarkus.arc.All;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Table(name = "todo")
@Data
public class Todo {// extends PanacheEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "completed")
    private Boolean completed;

    // @JoinColumn(name = "priority_id", foreignKey = @ForeignKey(name =
    // "priority_id_fk"))
    // @ManyToOne(optional = false, cascade = CascadeType.ALL)
    // @ManyToOne(optional = false)
    private Long priority_id;

    public Todo() {
    }

    public Todo(Long id, String title, Boolean completed, Priority priority) {
        this.id = id;
        this.title = title;
        this.completed = completed;
        this.priority_id = priority.getPriorityId();
    }
}