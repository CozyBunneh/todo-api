package com.example.todo;

public record CreateTodoV1(String title, boolean completed, Long priorityId) {

    public Todo toEntity() {
        return new Todo(null, title(), completed(), fromPriorityId());
    }

    public Priority fromPriorityId() {
        if (priorityId() == PriorityV1.Highest.id()) {
            return PriorityV1.Highest.toEntity();
        } else if (priorityId() == PriorityV1.High.id()) {
            return PriorityV1.High.toEntity();
        } else if (priorityId() == PriorityV1.Medium.id()) {
            return PriorityV1.Medium.toEntity();
        } else if (priorityId() == PriorityV1.Low.id()) {
            return PriorityV1.Low.toEntity();
        } else {
            return PriorityV1.Lowest.toEntity();
        }
    }
}
