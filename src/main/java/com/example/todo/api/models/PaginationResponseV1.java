package com.example.todo.api.models;

import java.util.List;

public record PaginationResponseV1<T>(List<T> data, Integer pageIndex, Integer pageSize, Integer totalPages, Long totalItems) {}
