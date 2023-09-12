package com.example.todo.infrastructure.persistence.entities;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class PriorityTest {

    @Test
    void testPriority() {
        // Arrange
        Long id = 1L;
        var name = "Highest";

        // Act
        var priority = new Priority(id, name);

        // Assert
        Assertions.assertNotNull(priority);
        Assertions.assertEquals(id, priority.getPriorityId());
        Assertions.assertEquals(name, priority.getName());
    }
}
