package com.example.todo.api.models;

import com.example.todo.infrastructure.persistence.entities.Priority;
import com.example.todo.infrastructure.persistence.entities.Todo;

public record CreateTodoV1(String title, boolean completed, Long priorityId) {

    public Todo toEntity() {
        return new Todo(null, title(), completed(), PriorityV1.fromId(priorityId()).toEntity());
    }
}
