package com.example.todo.infrastructure.persistence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.todo.infrastructure.persistence.entities.QueryType;
import com.example.todo.infrastructure.persistence.entities.Todo;
import com.example.todo.infrastructure.persistence.entities.Tuple;
import com.example.todo.api.models.PaginationResponseV1;
import io.quarkus.hibernate.reactive.panache.PanacheQuery;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class TodoRepository implements PanacheRepository<Todo> {

    @WithTransaction
    public Uni<PaginationResponseV1<Todo>> getAllPaginated(Integer pageIndex, Integer pageSize,
                                                           Optional<String> searchQuery, Optional<Boolean> filterCompleted) {
        Map<String, Tuple<QueryType, Object>> parameters = new HashMap<>();
        addIfNotNull(parameters, QueryType.Like, "title", searchQuery);
        addIfNotNull(parameters, QueryType.Equals, "completed", filterCompleted);
        String query = parameters.entrySet().stream()
                .map(x -> String.format("%s %s :%s",
                        QueryType.Like.id().equals(x.getValue().x().id()) ? "lower(" + x.getKey() + ")" : x.getKey(),
                        x.getValue().x().sqlCompareSymbol(),
                        x.getKey()))
                .collect(Collectors.joining(" and "));
        Map<String, Object> paramsMap = transformParameterMapToCorrectFormat(parameters);
        var totalUni = count(query, paramsMap);
        PanacheQuery<Todo> todosQuery = find(query, Sort.by("id"), paramsMap);
        Uni<List<Todo>> todosPagedQuery = todosQuery.page(pageIndex, pageSize).list();
        var todosUni = todosPagedQuery.onItem().transform(t -> {
            if (t == null) {
                throw new NotFoundException("Todos could not be found");
            }
            return t;
        });
        // Run the unis sequentially, this can also be done with
        // `Uni.join().all(totalUni, todosUni).usingConcurrency(1)`.
        return totalUni.flatMap(total -> todosUni.map(todos -> {
            double lastPageExact = (double) total / pageSize;
            Integer lastPage = (int) Math.ceil(lastPageExact);
            return new PaginationResponseV1<Todo>(todos, pageIndex, pageSize, lastPage, total);
        }));
    }

    private static <T> void addIfNotNull(Map<String, Tuple<QueryType, Object>> map, QueryType queryType, String key,
            Optional<T> value) {
        value.ifPresent(v -> map.put(key, new Tuple<>(queryType, v)));
    }

    private static Map<String, Object> transformParameterMapToCorrectFormat(
            Map<String, Tuple<QueryType, Object>> parameters) {
        return parameters.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> QueryType.Like.id().equals(e.getValue().x().id())
                                ? "%" + e.getValue().y() + "%"
                                : e.getValue().y()));
    }

    @WithTransaction
    public Uni<Todo> getById(Long id) {
        Uni<Todo> todo = findById(id);
        return todo.onItem().transform(t -> {
            if (t == null) {
                throw new NotFoundException("Todo with id " + id + " could not be found");
            }
            return t;
        });
    }

    @WithTransaction
    public Uni<Todo> create(Todo todo) {
        Uni<Todo> todoUni = findById(todo.id);
        return todoUni.onItem().transform(t -> {
            if (t != null) {
                throw new IllegalArgumentException("The Todo already exists.");
            }
            return todo;
        }).call(this::persist);
    }

    @WithTransaction
    public Uni<Todo> update(Todo todo) {
        Uni<Todo> todoUni = findById(todo.id);
        return todoUni.onItem().transform(t -> {
            if (t == null) {
                throw new IllegalArgumentException("Todo with id " + todo.id + " doesn't exist.");
            }
            return todo;
        }).call(t -> update("title = ?1, completed = ?2, priority_id = ?3 WHERE id = ?4", todo.title,
                todo.completed,
                todo.priority_id, todo.id));
    }

    @WithTransaction
    public Uni<Void> delete(Long id) {
        return deleteById(id).onItem().transform(result -> {
            if (!result) {
                throw new NotFoundException("Todo with id" + id + " was not found.");
            }
            return null;
        });
    }

    @WithTransaction
    public Uni<Void> deleteByCompleted(Boolean completed) {
        return delete("WHERE completed = ?1", completed).onItem().transform(result -> {
            return null;
        });
    }
}