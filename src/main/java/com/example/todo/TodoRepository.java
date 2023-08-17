package com.example.todo;

import java.util.List;

import io.quarkus.hibernate.reactive.panache.PanacheQuery;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class TodoRepository implements PanacheRepository<Todo> {

    @WithTransaction
    public Uni<PaginationResponseV1<Todo>> getAllPaginated(Integer pageIndex, Integer pageSize) {
        var totalUni = count();
        PanacheQuery<Todo> todosQuery = find("");
        Uni<List<Todo>> todosPagedQuery = todosQuery.page(pageIndex, pageSize).list();
        var todosUni = todosPagedQuery.onItem().transform(t -> {
            if (t == null) {
                throw new NotFoundException("Todos could not be found");
            }
            return t;
        });
        // Run the unis sequentially, this can also be done with `Uni.join().all(totalUni, todosUni).usingConcurrency(1)`.
        return totalUni.flatMap(total -> todosUni.map(todos -> {
            double lastPageExact = (double)total / pageSize;
            Integer lastPage = (int)Math.ceil(lastPageExact);
            return new PaginationResponseV1<Todo>(todos, pageIndex, pageSize, lastPage, total);
        }));
    }

    @WithTransaction
    public Uni<Todo> getById(Long id) {
        // Uni<Todo> todo = Todo.findById(id);

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
        // Uni<Todo> todoUni = Todo.findById(todo.id);
        Uni<Todo> todoUni = findById(todo.id);
        return todoUni.onItem().transform(t -> {
            if (t != null) {
                throw new IllegalArgumentException("The Todo already exists.");
            }
            // createPrioritySilently(todo.priority);
            // Todo newTodo = new Todo();
            // newTodo.title = todo.title;
            // newTodo.completed = todo.completed;
            // return newTodo;
            return todo;
        }).call(t -> persist(t));
    }

    // @WithTransaction
    // public Uni<Void> createPriority(Priority priority) {
    // Uni<Priority> priorityUni = Priority.findById(priority.priorityId);
    // return priorityUni.onItem().transform(p -> {
    // if (p != null) {
    // throw new IllegalArgumentException("The Priority already exists.");
    // }
    // return priority;
    // }).call(p -> Priority.persist(p)).replaceWithVoid();
    // }

    // private Uni<Void> createPrioritySilently(Priority priority) {
    // try {
    // return createPriority(priority);
    // } catch (IllegalArgumentException e) {
    // return Uni.createFrom().item(null);
    // } catch (ConstraintViolationException e) {
    // return Uni.createFrom().item(null);
    // }
    // }

    @WithTransaction
    public Uni<Todo> update(Todo todo) {
        Uni<Todo> todoUni = findById(todo.id);
        return todoUni.onItem().transform(t -> {
            if (t == null) {
                throw new IllegalArgumentException("Todo with id " + todo.id + " doesn't exist.");
            }
            // createPrioritySilently(todo.priority);
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