package com.example.todo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "priority")
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