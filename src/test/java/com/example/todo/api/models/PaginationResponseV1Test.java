package com.example.todo.api.models;

import com.example.todo.infrastructure.persistence.entities.Priority;
import com.example.todo.infrastructure.persistence.entities.Todo;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@QuarkusTest
public class PaginationResponseV1Test {

    @Test
    void testPaginationResponseV1() {
        // Arrange
        List<TodoV1> data = List.of(new TodoV1(1L, "something", false, PriorityV1.Lowest));
        Integer pageIndex = 0;
        Integer pageSize = 5;
        Integer totalPages = 200;
        Long totalItems = 1000L;

        // Act
        var paginationResponse = new PaginationResponseV1<TodoV1>(data, pageIndex, pageSize, totalPages, totalItems);

        // Assert
        Assertions.assertNotNull(paginationResponse);
        Assertions.assertNotNull(paginationResponse.data());
        Assertions.assertEquals(1, paginationResponse.data().size());
        Assertions.assertNotNull(paginationResponse.data().get(0));
        Assertions.assertEquals(1L, paginationResponse.data().get(0).id());
        Assertions.assertEquals("something", paginationResponse.data().get(0).title());
        Assertions.assertFalse(paginationResponse.data().get(0).completed());
        Assertions.assertEquals(PriorityV1.Lowest, paginationResponse.data().get(0).priority());
    }
}
