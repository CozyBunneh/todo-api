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

    @Test
    void testFromIdHighest() {
        // Arrange
        var id = 1L;

        // Act
        var priority = PriorityV1.fromId(id);

        // Assert
        Assertions.assertNotNull(priority);
        Assertions.assertEquals(id, priority.id());
        Assertions.assertEquals("Highest", priority.name());
    }

    @Test
    void testFromIdHigh() {
        // Arrange
        var id = 2L;

        // Act
        var priority = PriorityV1.fromId(id);

        // Assert
        Assertions.assertNotNull(priority);
        Assertions.assertEquals(id, priority.id());
        Assertions.assertEquals("High", priority.name());
    }

    @Test
    void testFromIdMedium() {
        // Arrange
        var id = 3L;

        // Act
        var priority = PriorityV1.fromId(id);

        // Assert
        Assertions.assertNotNull(priority);
        Assertions.assertEquals(id, priority.id());
        Assertions.assertEquals("Medium", priority.name());
    }

    @Test
    void testFromIdLow() {
        // Arrange
        var id = 4L;

        // Act
        var priority = PriorityV1.fromId(id);

        // Assert
        Assertions.assertNotNull(priority);
        Assertions.assertEquals(id, priority.id());
        Assertions.assertEquals("Low", priority.name());
    }

    @Test
    void testFromIdLowest() {
        // Arrange
        var id = 5L;

        // Act
        var priority = PriorityV1.fromId(id);

        // Assert
        Assertions.assertNotNull(priority);
        Assertions.assertEquals(id, priority.id());
        Assertions.assertEquals("Lowest", priority.name());
    }
}
