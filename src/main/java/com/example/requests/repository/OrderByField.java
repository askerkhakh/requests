package com.example.requests.repository;

public class OrderByField {

    private final String field;
    private final Direction direction;

    public OrderByField(String field, Direction direction) {
        this.field = field;
        this.direction = direction;
    }

    public String getField() {
        return field;
    }

    public Direction getDirection() {
        return direction;
    }

    public enum Direction { ASCENDING, DESCENDING }
}
