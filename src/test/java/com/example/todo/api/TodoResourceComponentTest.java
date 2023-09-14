package com.example.todo.api;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import com.example.todo.api.models.PriorityV1;
import com.example.todo.api.models.CreateTodoV1;
import com.example.todo.infrastructure.persistence.TodoRepository;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@QuarkusTest
@TestMethodOrder(OrderAnnotation.class)
public class TodoResourceComponentTest {

    @Inject
    TodoRepository todoRepository;

    @Test
    @Order(1)
    void setup() {
        // Inject 10 todos to test on if they aren't already there
        try {
            given()
                    .queryParam("page_index", 0)
                    .queryParam("page_size", 10)
                    .when().get("/todos")
                    .then()
                    .statusCode(equalTo(200))
                    .body("pageIndex", is(0))
                    .body("pageSize", is(10))
                    .body("totalItems", is(0))
                    .body("totalPages", is(0));
        } catch (AssertionError e) {
            return;
        }

        var todo1 = new CreateTodoV1("todo1", false, PriorityV1.Lowest.id());
        var todo2 = new CreateTodoV1("todo2", false, PriorityV1.Low.id());
        var todo3 = new CreateTodoV1("todo3", false, PriorityV1.Medium.id());
        var todo4 = new CreateTodoV1("todo4", false, PriorityV1.High.id());
        var todo5 = new CreateTodoV1("todo5", false, PriorityV1.Highest.id());
        var todo6 = new CreateTodoV1("todo6", true, PriorityV1.Lowest.id());
        var todo7 = new CreateTodoV1("todo7", true, PriorityV1.Low.id());
        var todo8 = new CreateTodoV1("todo8", true, PriorityV1.Medium.id());
        var todo9 = new CreateTodoV1("todo9", true, PriorityV1.High.id());
        var todo10 = new CreateTodoV1("todo10", true, PriorityV1.Highest.id());
        createTodo(todo1, 1L);
        createTodo(todo2, 2L);
        createTodo(todo3, 3L);
        createTodo(todo4, 4L);
        createTodo(todo5, 5L);
        createTodo(todo6, 6L);
        createTodo(todo7, 7L);
        createTodo(todo8, 8L);
        createTodo(todo9, 9L);
        createTodo(todo10, 10L);
    }

    private void createTodo(CreateTodoV1 createTodo, Long expectedId) {
        given()
                .when()
                .contentType(ContentType.JSON)
                .body(createTodo)
                .put("/todos")
                .then()
                .statusCode(201);
    }

    @Test
    @Order(2)
    void testGetPaginationSuccess() {
        // Arrange
        Integer pageIndex = 1;
        Integer pageSize = 3;

        // Act & Assert
        given()
                .queryParam("page_index", pageIndex)
                .queryParam("page_size", pageSize)
                .when().get("/todos")
                .then()
                .statusCode(equalTo(200))
                .body("data[0].completed", is(false))
                .body("data[0].priority.id", is(2))
                .body("data[0].priority.name", is("High"))
                .body("data[0].title", is("todo4"))
                .body("data[1].completed", is(false))
                .body("data[1].priority.id", is(1))
                .body("data[1].priority.name", is("Highest"))
                .body("data[1].title", is("todo5"))
                .body("data[2].completed", is(true))
                .body("data[2].priority.id", is(5))
                .body("data[2].priority.name", is("Lowest"))
                .body("data[2].title", is("todo6"))
                .body("pageIndex", is(1))
                .body("pageSize", is(3))
                .body("totalItems", is(10))
                .body("totalPages", is(4));
    }

    @Test
    @Order(3)
    void testGetById() {
        // Arrange
        Long id = 1L;

        // Act & Assert
        given()
                .pathParam("id", id)
                .when().get("/todos/{id}")
                .then()
                .statusCode(200)
                .body("id", is(1))
                .body("completed", is(false))
                .body("priority.id", is(5))
                .body("priority.name", is("Lowest"))
                .body("title", is("todo1"));
    }

    @Test
    @Order(4)
    void testCreate() {
        // Arrange
        CreateTodoV1 createTodo = new CreateTodoV1("something", false, PriorityV1.Lowest.id());

        // Act & Assert
        given()
                .when()
                .contentType(ContentType.JSON)
                .body(createTodo)
                .put("/todos")
                .then()
                .statusCode(201)
                .header("location", is("http://localhost:8081/todos/11"));

        // Cleanup
        given()
                .when()
                .pathParam("id", 11L)
                .delete("/todos/{id}")
                .then()
                .statusCode(200);
    }

    @Test
    @Order(4)
    void testDelete() {
        // Arrange
        CreateTodoV1 createTodo = new CreateTodoV1("something", false, PriorityV1.Lowest.id());
        given()
                .when()
                .contentType(ContentType.JSON)
                .body(createTodo)
                .put("/todos")
                .then()
                .statusCode(201)
                .header("location", is("http://localhost:8081/todos/12"));

        // Act & Assert
        given()
                .when()
                .pathParam("id", 12L)
                .delete("/todos/{id}")
                .then()
                .statusCode(200);
    }
}