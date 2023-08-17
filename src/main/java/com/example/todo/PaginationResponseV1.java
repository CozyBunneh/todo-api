package com.example.todo;

import java.util.List;

public record PaginationResponseV1<T>(List<T> data, Integer pageIndex, Integer pageSize, Integer totalPages, Long totalItems) {}
