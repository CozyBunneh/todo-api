package com.example.todo;

import java.net.URI;
import java.util.Optional;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import javax.swing.text.html.Option;

@Path("/todos")
public class TodoResource {

    @Inject
    TodoRepository repository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<PaginationResponseV1<TodoV1>> getPaginated(@QueryParam("page_index") Integer pageIndex,
                                                          @QueryParam("page_size") Integer pageSize,
                                                          @QueryParam("query") String query,
                                                          @QueryParam("filter_by_completed") Boolean filterByCompleted) {
        Optional<String> filterQuery = query == null || query.isEmpty()
                ? Optional.empty()
                : Optional.of(query.trim());
        Optional<Boolean> filterCompleted = filterByCompleted == null
                ? Optional.empty()
                : Optional.of(filterByCompleted);
        return repository.getAllPaginated(pageIndex, pageSize, filterQuery, filterCompleted).onItem().transform(paginatedData -> {
            if (paginatedData == null) {
                throw new NotFoundException("Paginated data could not be found");
            }
            return new PaginationResponseV1<TodoV1>(TodoV1.fromEntities(paginatedData.data()), paginatedData.pageIndex(), paginatedData.pageSize(), paginatedData.totalPages(), paginatedData.totalItems());
        });
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<TodoV1> getById(Long id) {
        return TodoV1.fromEntity(repository.getById(id));
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> create(CreateTodoV1 createTodo) {
        return repository.create(createTodo.toEntity()).onItem().transform(todo -> URI.create("/todos/" + todo.id))
                .onItem().transform(uri -> Response.created(uri).build());
    }

    @POST
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> update(Long id, TodoV1 todo) {
        return repository.update(todo.toEntity()).onItem().transform(t -> Response.ok().build());
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> delete(Long id) {
        return repository.delete(id).onItem().transform(t -> Response.ok().build());
    }

    @DELETE
    @Path("/completed")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> delete() {
        return repository.deleteByCompleted(true).onItem().transform(t -> Response.ok().build());
    }
}