package com.example.todo.infrastructure.persistence.entities;

import com.example.todo.api.models.PriorityV1;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class TodoTest {

    @Test
    void testTodoV1() {
        // Arrange
        var id = 1L;
        var title = "something";
        var completed = false;
        var priorityId = PriorityV1.Low.toEntity();

        // Act
        var todo = new Todo(id, title, completed, priorityId);

        // Assert
        Assertions.assertNotNull(todo);
        Assertions.assertEquals(id, todo.getId());
        Assertions.assertEquals(title, todo.getTitle());
        Assertions.assertEquals(completed, todo.getCompleted());
        Assertions.assertEquals(priorityId.getPriorityId(), todo.getPriority_id());
    }
}
