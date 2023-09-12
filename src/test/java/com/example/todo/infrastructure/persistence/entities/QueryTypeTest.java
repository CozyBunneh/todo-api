package com.example.todo.infrastructure.persistence.entities;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.QueryParam;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class QueryTypeTest {

    @Test
    void testQueryType() {
        // Arrange
        var id = 1;
        var name = "Equals";
        var sqlCompareSymbol = "=";

        // Act
        var queryType = new QueryType(id, name, sqlCompareSymbol);

        // Assert
        Assertions.assertNotNull(queryType);
        Assertions.assertEquals(id, queryType.id());
        Assertions.assertEquals(name, queryType.name());
        Assertions.assertEquals(sqlCompareSymbol, queryType.sqlCompareSymbol());
    }

    @Test
    void testToQueryEquals() {
        // Arrange
        var key = "column_name";
        var value = "value";
        var queryType = QueryType.Equals;

        // Act
        var queryString = queryType.toQuery(key, value);

        // Assert
        Assertions.assertNotNull(queryString);
        Assertions.assertEquals(":" + key, queryString);
    }

    @Test
    void testToQueryLike() {
        // Arrange
        var key = "column_name";
        var value = "value";
        var queryType = QueryType.Like;

        // Act
        var queryString = queryType.toQuery(key, value);

        // Assert
        Assertions.assertNotNull(queryString);
        Assertions.assertEquals("%" + value + "%", queryString);
    }
}
