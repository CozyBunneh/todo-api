package com.example.todo.api.models;

import java.util.List;
import java.util.stream.Collectors;

import com.example.todo.infrastructure.persistence.entities.Todo;
import io.smallrye.mutiny.Uni;

public record TodoV1(Long id, String title, boolean completed, PriorityV1 priority) {

    public Todo toEntity() {
        return new Todo(id(), title(), completed(), priority.id());
    }

    public static Uni<List<TodoV1>> fromEntities(Uni<List<Todo>> todos) {
        return todos.map(TodoV1::fromEntities);
    }

    public static List<TodoV1> fromEntities(List<Todo> todos) {
        return todos.stream().map(TodoV1::fromEntity).collect(Collectors.toList());
    }

    public static Uni<TodoV1> fromEntity(Uni<Todo> todo) {
        return todo.map(TodoV1::fromEntity);
    }

    public static TodoV1 fromEntity(Todo todo) {
        return new TodoV1(todo.getId(), todo.getTitle(), todo.getCompleted(), PriorityV1.fromId(todo.getPriority_id()));
    }
}