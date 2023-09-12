package com.example.todo.api.models;

import com.example.todo.infrastructure.persistence.entities.Priority;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
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
    void testToEntity() {
        // Arrange
        var priority = PriorityV1.Lowest;

        // Act
        var entity = priority.toEntity();

        // Assert
        Assertions.assertNotNull(entity);
        Assertions.assertEquals(priority.id(), entity.getPriorityId());
        Assertions.assertEquals(priority.name(), entity.getName());
    }

    @Test
    void testFromEntity() {
        // Arrange
        var entity = new Priority(1L, "Highest");

        // Act
        var priority = PriorityV1.fromEntity(entity);

        // Assert
        Assertions.assertNotNull(priority);
        Assertions.assertEquals(entity.getPriorityId(), priority.id());
        Assertions.assertEquals(entity.getName(), priority.name());
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
