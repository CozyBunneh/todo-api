package com.example.todo.api.models;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class CreateTodoV1Test {

    @Test
    void testCreateTodoV1() {
        // Arrange
        var title = "something";
        var completed = false;
        var priorityId = PriorityV1.Low.id();

        // Act
        var createTodo = new CreateTodoV1(title, completed, priorityId);

        // Assert
        Assertions.assertNotNull(createTodo);
        Assertions.assertEquals(title, createTodo.title());
        Assertions.assertEquals(completed, createTodo.completed());
        Assertions.assertEquals(priorityId, createTodo.priorityId());
    }

    @Test
    void toEntity() {
        // Arrange
        var title = "something";
        var completed = false;
        var priorityId = PriorityV1.Low.id();

        // Act
        var todo = new CreateTodoV1(title, completed, priorityId).toEntity();

        // Assert
        Assertions.assertNotNull(todo);
        Assertions.assertEquals(title, todo.getTitle());
        Assertions.assertEquals(completed, todo.getCompleted());
        Assertions.assertEquals(priorityId, todo.getPriority_id());
    }
}
