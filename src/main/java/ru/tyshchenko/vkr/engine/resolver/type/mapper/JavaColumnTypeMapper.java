package ru.tyshchenko.vkr.engine.resolver.type.mapper;

import ru.tyshchenko.vkr.engine.api.models.entity.types.ColumnType;

public class JavaColumnTypeMapper implements ColumnTypeMapper {

    @Override
    public Type mapType(ColumnType type) {
        switch (type) {
            case BIGINT, BIGSERIAL -> {
                return new Type(null, "Long");
            }
            case BOOLEAN -> {
                return new Type(null, "Boolean");
            }
            case INT -> {
                return new Type(null, "Integer");
            }
            case TEXT -> {
                return new Type(null, "String");
            }
            case NUMERIC -> {
                return new Type("java.math", "BigDecimal");
            }
            case DATE -> {
                return new Type("java.time", "LocalDate");
            }
            case TIMESTAMP -> {
                return new Type("java.time", "LocalDateTime");
            }
            default -> throw new UnsupportedOperationException();
        }
    }

    @Override
    public String language() {
        return "JAVA";
    }
}
