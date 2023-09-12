package com.example.todo.infrastructure.persistence;

import com.example.todo.api.models.PriorityV1;
import com.example.todo.infrastructure.persistence.entities.Todo;
import io.quarkus.hibernate.reactive.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;
import io.quarkus.test.InjectMock;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@QuarkusTest
@TestTransaction
public class TodoRepositoryTest {

    @InjectMock
    TodoRepository todoRepository;

    @Test
    void testGetAllPaginatedSuccess() {
        // Arrange
        when(todoRepository.getAllPaginated(anyInt(), anyInt(), any(), any())).thenCallRealMethod();
        when(todoRepository.count(anyString(), anyMap()))
                .thenReturn(Uni.createFrom().item(1000L));
        PanacheQuery query = mock(PanacheQuery.class);
        when(todoRepository.find(anyString(), any(Sort.class), anyMap()))
                .thenReturn(query);
        var todos = List.of(new Todo(1L, "title1", false, PriorityV1.Lowest.toEntity()));
        when(query.page(anyInt(), anyInt())).thenReturn(query);
        when(query.list())
                .thenReturn(Uni.createFrom().item(todos));

        // Act
        var paginatedTodos = todoRepository
                .getAllPaginated(
                        0,
                        5,
                        Optional.empty(),
                        Optional.empty())
                .await()
                .indefinitely();

        // Assert
        Assertions.assertNotNull(paginatedTodos);
        Assertions.assertEquals(1, paginatedTodos.data().size());
        Assertions.assertNotNull(paginatedTodos.data().get(0));
        Assertions.assertEquals(1L, paginatedTodos.data().get(0).getId());
        Assertions.assertEquals("title1", paginatedTodos.data().get(0).getTitle());
        Assertions.assertFalse(paginatedTodos.data().get(0).getCompleted());
        Assertions.assertEquals(PriorityV1.Lowest.id(), paginatedTodos.data().get(0).getPriority_id());
        Assertions.assertEquals(0, paginatedTodos.pageIndex());
        Assertions.assertEquals(5, paginatedTodos.pageSize());
        Assertions.assertEquals(200, paginatedTodos.totalPages());
        Assertions.assertEquals(1000L, paginatedTodos.totalItems());
    }

    @Test
    void testGetByIdSuccess() {
        // Arrange
        when(todoRepository.getById(anyLong())).thenCallRealMethod();
        var id = 1L;
        var todoResponse = new Todo(1L, "title1", false, PriorityV1.Lowest.toEntity());
        when(todoRepository.findById(anyLong()))
                .thenReturn(Uni.createFrom().item(todoResponse));

        // Act
        var todo = todoRepository.getById(id).await().indefinitely();

        // Assert
        Assertions.assertNotNull(todo);
        Assertions.assertEquals(id, todo.getId());
    }

    @Test
    void testCreateSuccess() {
        // Arrange
        when(todoRepository.create(any(Todo.class))).thenCallRealMethod();
        var todo = new Todo(null, "todo1", false, PriorityV1.Lowest.toEntity());
        when(todoRepository.findById(anyLong()))
                .thenReturn(Uni.createFrom().nullItem());
        when(todoRepository.persist(any(Todo.class)))
                .thenReturn(Uni.createFrom().item(new Todo(1L, "todo1", false, PriorityV1.Lowest.toEntity())));

        // Act
        var createdTodo = todoRepository.create(todo).await().indefinitely();

        // Assert
        Assertions.assertNotNull(createdTodo);
        Assertions.assertEquals(1L, createdTodo.getId());
        Assertions.assertEquals("todo1", createdTodo.getTitle());
        Assertions.assertFalse(createdTodo.getCompleted());
        Assertions.assertEquals(PriorityV1.Lowest.id(), createdTodo.getPriority_id());
    }

    @Test
    void testCreateAlreadyExists() {
        // Arrange
        when(todoRepository.create(any(Todo.class))).thenCallRealMethod();
        var todo = new Todo(1L, "todo1", false, PriorityV1.Lowest.toEntity());
        when(todoRepository.findById(anyLong()))
                .thenReturn(Uni.createFrom().item(new Todo(1L, "todo1", false, PriorityV1.Lowest.toEntity())));

        // Act & Assert
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> todoRepository.create(todo).await().indefinitely(),
                "The Todo already exists");
    }

    @Test
    void testUpdateSuccess() {
        // Arrange
        when(todoRepository.update(any(Todo.class))).thenCallRealMethod();
        var todo = new Todo(1L, "todo1", false, PriorityV1.Lowest.toEntity());
        when(todoRepository.findById(anyLong()))
                .thenReturn(Uni.createFrom().item(todo));
        when(todoRepository.update(anyString(), ArgumentMatchers.<Object>any()))
                .thenReturn(Uni.createFrom().item(1));

        // Act
        var updatedTodo = todoRepository.update(todo).await().indefinitely();

        // Assert
        Assertions.assertNotNull(updatedTodo);
        Assertions.assertEquals(1L, updatedTodo.getId());
        Assertions.assertEquals("todo1", updatedTodo.getTitle());
        Assertions.assertFalse(updatedTodo.getCompleted());
        Assertions.assertEquals(PriorityV1.Lowest.id(), updatedTodo.getPriority_id());
    }

    @Test
    void testUpdateNotExists() {
        // Arrange
        when(todoRepository.update(any(Todo.class))).thenCallRealMethod();
        var todo = new Todo(1L, "todo1", false, PriorityV1.Lowest.toEntity());
        when(todoRepository.findById(anyLong()))
                .thenReturn(Uni.createFrom().nullItem());
        when(todoRepository.update(anyString(), ArgumentMatchers.<Object>any()))
                .thenReturn(Uni.createFrom().item(1));

        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> todoRepository.update(todo).await().indefinitely(),
                "Todo with id '" + 1L + "' doesn't exist.");
    }

    @Test
    void testDeleteSuccess() {
        // Arrange
        when(todoRepository.delete(anyLong())).thenCallRealMethod();
        Long id = 1L;
        when(todoRepository.deleteById(anyLong()))
                .thenReturn(Uni.createFrom().item(true));

        // Act
        var deleted = todoRepository.delete(id).await().indefinitely();

        // Assertion
        Assertions.assertNull(deleted);
    }

    @Test
    void testDeleteNotFound() {
        // Arrange
        when(todoRepository.delete(anyLong())).thenCallRealMethod();
        Long id = 1L;
        when(todoRepository.deleteById(anyLong()))
                .thenReturn(Uni.createFrom().item(false));

        // Act & Assert
        Assertions.assertThrows(
                NotFoundException.class,
                () -> todoRepository.delete(id).await().indefinitely(),
                "Todo with id '" + id + "' was not found");
    }

    @Test
    void testDeleteByCompletedSuccess() {
        // Arrange
        when(todoRepository.deleteByCompleted(anyBoolean())).thenCallRealMethod();
        when(todoRepository.delete(anyString(), ArgumentMatchers.<Object>any()))
                .thenReturn(Uni.createFrom().item(1L));

        // Act
        var deleted = todoRepository.deleteByCompleted(true).await().indefinitely();

        // Assert
        Assertions.assertNull(deleted);
    }
}
