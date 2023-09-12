package com.example.todo.infrastructure.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "priority")
@Data
public class Priority {
    @Id
    @Column(name = "id")
    public Long priorityId;

    @Column(name = "name")
    public String name;

    public Priority() {
    }

    public Priority(Long id, String name) {
        this.priorityId = id;
        this.name = name;
    }
}