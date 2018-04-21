package com.d3.datawrappers.common;

public enum AttributeType {
    BOOLEAN("b"),
    DATE("d"),
    DATETIME("t"),
    DECIMAL("c"),
    INTEGER("i"),
    MONEY("m"),
    OBSCURE("o"),
    PERCENT("p"),
    RESOURCE("r"),
    STRING("s"),
    URL("u");

    private String conduitCode;

    AttributeType(String conduitCode) {
        this.conduitCode = conduitCode;
    }

    public String getConduitCode() {
        return conduitCode;
    }
}
