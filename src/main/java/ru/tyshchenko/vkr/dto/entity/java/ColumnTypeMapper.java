package ru.tyshchenko.vkr.dto.entity.java;

import lombok.NonNull;
import org.springframework.stereotype.Component;
import ru.tyshchenko.vkr.dto.entity.types.ColumnType;

@Component
public class ColumnTypeMapper {

    public Type mapType(ColumnType columnType) {
        switch (columnType) {
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

    public record Type(String packet, @NonNull String type) {
    }
}
