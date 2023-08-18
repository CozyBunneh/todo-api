package com.example.todo;

public record CreateTodoV1(String title, boolean completed, Long priorityId) {

    public Todo toEntity() {
        return new Todo(null, title(), completed(), fromPriorityId());
    }

    public Priority fromPriorityId() {
        if (PriorityV1.Highest.id().equals(priorityId())) {
            return PriorityV1.Highest.toEntity();
        } else if (PriorityV1.High.id().equals(priorityId())) {
            return PriorityV1.High.toEntity();
        } else if (PriorityV1.Medium.id().equals(priorityId())) {
            return PriorityV1.Medium.toEntity();
        } else if (PriorityV1.Low.id().equals(priorityId())) {
            return PriorityV1.Low.toEntity();
        } else {
            return PriorityV1.Lowest.toEntity();
        }
    }
}
