package com.example.todo;

public record QueryType(Integer id, String name, String sqlCompareSymbol) {

    public static QueryType Equals = new QueryType(0, "Equals", "=");
    public static QueryType Like = new QueryType(0, "Like", "like");

    public String toQuery(String key, Object value) {
        return QueryType.Like.id().equals(this.id())
                ? "%" + value.toString() + "%"
                : ":" + key;
    }
}
