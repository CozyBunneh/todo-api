package com.example.todo.infrastructure.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "todo")
@Data
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "completed")
    private Boolean completed;

    @Column(name = "priority_id")
    private Long priority_id;

    public Todo() {
    }

    public Todo(Long id, String title, Boolean completed, Long priorityId) {
        this.id = id;
        this.title = title;
        this.completed = completed;
        this.priority_id = priorityId;
    }
}