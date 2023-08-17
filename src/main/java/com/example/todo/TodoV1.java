package com.example.todo;

import java.util.List;
import java.util.stream.Collectors;

import io.smallrye.mutiny.Uni;

public record TodoV1(Long id, String title, boolean completed, PriorityV1 priority) {

    public Todo toEntity() {
        return new Todo(id(), title(), completed(), priority.toEntity());
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
        return new TodoV1(todo.id, todo.title, todo.completed, PriorityV1.fromId(todo.priority_id));
    }
}