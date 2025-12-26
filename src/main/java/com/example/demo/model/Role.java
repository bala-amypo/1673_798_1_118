package com.example.demo.model;

public enum Role {
    ADMIN("ADMIN"),
    ANALYST("ANALYST"),
    MANAGER("MANAGER"),
    USER("USER");

    private final String value;

    Role(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public String getValue() {
        return value;
    }
}
