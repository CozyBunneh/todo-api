package com.example.todo.api;

import com.example.todo.api.models.CreateTodoV1;
import com.example.todo.api.models.PaginationResponseV1;
import com.example.todo.api.models.PriorityV1;
import com.example.todo.api.models.TodoV1;
import com.example.todo.infrastructure.persistence.TodoRepository;
import com.example.todo.infrastructure.persistence.entities.Todo;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@QuarkusTest
public class TodoResourceTest {

    @InjectMock
    TodoRepository todoRepository;

    @Test
    void testGetPaginationSuccess() {
        // Arrange
        List<Todo> data = List.of(new Todo(1L, "something", false, PriorityV1.Lowest.id()));
        Integer pageIndex = 0;
        Integer pageSize = 5;
        Integer totalPages = 200;
        Long totalItems = 1000L;
        var paginationResponse = new PaginationResponseV1<Todo>(data, pageIndex, pageSize, totalPages, totalItems);
        when(todoRepository.getAllPaginated(anyInt(), anyInt(), any(), any()))
                .thenReturn(Uni.createFrom().item(paginationResponse));

        // Act & Assert
        given()
                .queryParam("page_index", 0)
                .queryParam("page_size", 5)
                .when().get("/todos")
                .then()
                .statusCode(equalTo(200))
                .body("data[0].completed", is(false))
                .body("data[0].id", is(1))
                .body("data[0].priority.id", is(5))
                .body("data[0].priority.name", is("Lowest"))
                .body("data[0].title", is("something"))
                .body("pageIndex", is(0))
                .body("pageSize", is(5))
                .body("totalItems", is(1000))
                .body("totalPages", is(200));
    }

    @Test
    void testGetPaginationNotFound() {
        // Arrange
        when(todoRepository.getAllPaginated(anyInt(), anyInt(), any(), any()))
                .thenReturn(Uni.createFrom().nullItem());

        // Act & Assert
        given()
                .queryParam("page_index", 0)
                .queryParam("page_size", 5)
                .when().get("/todos")
                .then()
                .statusCode(equalTo(404));
    }

    @Test
    void testGetByIdSuccess() {
        // Arrange
        Long id = 1L;
        Todo todo = new Todo(id, "something", false, PriorityV1.Lowest.id());
        when(todoRepository.getById(1L))
                .thenReturn(Uni.createFrom().item(todo));

        // Act & Assert
        given()
                .pathParam("id", id)
                .when().get("/todos/{id}")
                .then()
                .statusCode(200)
                .body("completed", is(false))
                .body("id", is(1))
                .body("priority.id", is(5))
                .body("priority.name", is("Lowest"))
                .body("title", is("something"));
    }

    @Test
    void testCreateSuccess() {
        // Arrange
        CreateTodoV1 createTodo = new CreateTodoV1("something", false, PriorityV1.Lowest.id());
        when(todoRepository.create(any(Todo.class)))
                .thenReturn(Uni.createFrom().item(new Todo(1L, "something", false, PriorityV1.Lowest.id())));

        // Act & Assert
        given()
                .when()
                .contentType(ContentType.JSON)
                .body(createTodo)
                .put("/todos")
                .then()
                .statusCode(201)
                .header("location", is("http://localhost:8081/todos/1"));
    }

    @Test
    void testUpdateSuccess() {
        // Arrange
        Long id = 1L;
        TodoV1 todo = new TodoV1(id, "something", false, PriorityV1.Lowest);
        when(todoRepository.update(any(Todo.class)))
                .thenReturn(Uni.createFrom().item(todo.toEntity()));

        // Act & Assert
        given()
                .when()
                .contentType(ContentType.JSON)
                .body(todo)
                .pathParam("id", id)
                .post("/todos/{id}")
                .then()
                .statusCode(200);
    }

    @Test
    void testDeleteByIdSuccess() {
        // Arrange
        Long id = 1L;
        when(todoRepository.delete(anyLong()))
                .thenReturn(Uni.createFrom().voidItem());

        // Act & Assert
        given()
                .when()
                .pathParam("id", id)
                .delete("/todos/{id}")
                .then()
                .statusCode(200);
    }

    @Test
    void testDeleteCompletedSuccess() {
        // Arrange
        when(todoRepository.deleteByCompleted(anyBoolean()))
                .thenReturn(Uni.createFrom().voidItem());

        // Act & Assert
        given()
                .when()
                .delete("/todos/completed")
                .then()
                .statusCode(200);
    }
}
