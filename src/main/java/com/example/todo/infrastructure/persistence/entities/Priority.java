package com.example.todo.infrastructure.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Table(name = "priority")
@Data
@AllArgsConstructor
public class Priority {
    @Id
    @Column(name = "id")
    private Long priorityId;

    @Column(name = "name")
    private String name;
}