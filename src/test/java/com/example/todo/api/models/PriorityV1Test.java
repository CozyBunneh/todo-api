package com.example.todo.api.models;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class PriorityV1Test {

    @Test
    void testPriorityV1() {
        // Arrange
        Long id = 1L;
        var name = "something";

        // Act
        var priority = new PriorityV1(id, name);

        // Assert
        Assertions.assertNotNull(priority);
        Assertions.assertEquals(id, priority.id());
        Assertions.assertEquals(name, priority.name());
    }
}
