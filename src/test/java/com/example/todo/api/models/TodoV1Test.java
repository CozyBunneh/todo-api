package com.example.todo.api.models;

import com.example.todo.infrastructure.persistence.entities.Todo;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

@QuarkusTest
public class TodoV1Test {

    @Test
    void testTodoV1() {
        // Arrange
        Long id = 1L;
        var title = "something";
        var completed = false;
        var priority = PriorityV1.Lowest;

        // Act
        var todo = new TodoV1(id, title, completed, priority);

        // Assert
        Assertions.assertNotNull(todo);
        Assertions.assertEquals(id, todo.id());
        Assertions.assertEquals(title, todo.title());
        Assertions.assertEquals(completed, todo.completed());
        Assertions.assertEquals(priority, todo.priority());
    }

    @Test
    void testToEntity() {
        // Arrange
        Long id = 1L;
        var title = "something";
        var completed = false;
        var priority = PriorityV1.Lowest;

        // Act
        var todo = new TodoV1(id, title, completed, priority).toEntity();

        // Assert
        Assertions.assertNotNull(todo);
        Assertions.assertEquals(id, todo.getId());
        Assertions.assertEquals(title, todo.getTitle());
        Assertions.assertEquals(completed, todo.getCompleted());
        Assertions.assertEquals(priority.id(), todo.getPriority_id());
    }

    @Test
    void testFromEntities() {
        // Arrange
        Long id = 1L;
        var title = "something";
        var completed = false;
        var priority = PriorityV1.Lowest;
        var todos = List.of(new Todo(id, title, completed, priority.toEntity()));

        // Act
        var todosV1 = TodoV1.fromEntities(todos);

        // Assert
        Assertions.assertNotNull(todosV1);
        Assertions.assertEquals(1, todosV1.size());
        Assertions.assertNotNull(todosV1.get(0));
        Assertions.assertEquals(id, todosV1.get(0).id());
        Assertions.assertEquals(title, todosV1.get(0).title());
        Assertions.assertEquals(completed, todosV1.get(0).completed());
        Assertions.assertEquals(priority, todosV1.get(0).priority());
    }

    @Test
    void testFromEntitiesUni() {
        // Arrange
        Long id = 1L;
        var title = "something";
        var completed = false;
        var priority = PriorityV1.Lowest;
        var todos = Uni.createFrom().item(List.of(new Todo(id, title, completed, priority.toEntity())));

        // Act
        var todosV1 = TodoV1.fromEntities(todos).await().indefinitely();

        // Assert
        Assertions.assertNotNull(todosV1);
        Assertions.assertEquals(1, todosV1.size());
        Assertions.assertNotNull(todosV1.get(0));
        Assertions.assertEquals(id, todosV1.get(0).id());
        Assertions.assertEquals(title, todosV1.get(0).title());
        Assertions.assertEquals(completed, todosV1.get(0).completed());
        Assertions.assertEquals(priority, todosV1.get(0).priority());
    }
}
