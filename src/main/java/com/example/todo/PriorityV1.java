package com.example.todo;

import io.smallrye.mutiny.Uni;

public record PriorityV1(Long id, String name) {

    public static PriorityV1 Highest = new PriorityV1(1L, "Higest");
    public static PriorityV1 High = new PriorityV1(2L, "High");
    public static PriorityV1 Medium = new PriorityV1(3L, "Medium");
    public static PriorityV1 Low = new PriorityV1(4L, "Low");
    public static PriorityV1 Lowest = new PriorityV1(5L, "Lowest");

    public Priority toEntity() {
        return new Priority(id(), name());
    }

    public static Uni<PriorityV1> fromEntity(Uni<Priority> priority) {
        return priority.map(p -> fromEntity(p));
    }

    public static PriorityV1 fromEntity(Priority priority) {
        var id = priority.priorityId;
        if (Highest.id().equals(id)) {
            return Highest;
        } else if (High.id().equals(id)) {
            return High;
        } else if (Medium.id().equals(id)) {
            return Medium;
        } else if (Low.id().equals(id)) {
            return Low;
        } else {
            return Lowest;
        }
    }

    public static PriorityV1 fromId(Long id) {
        if (Highest.id().equals(id)) {
            return Highest;
        } else if (High.id().equals(id)) {
            return High;
        } else if (Medium.id().equals(id)) {
            return Medium;
        } else if (Low.id().equals(id)) {
            return Low;
        } else {
            return Lowest;
        }
    }
}