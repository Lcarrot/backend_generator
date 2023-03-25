package ru.tyshchenko.vkr.entity.java;

import org.springframework.stereotype.Component;
import ru.tyshchenko.vkr.entity.types.ColumnType;

@Component
public class ColumnTypeMapper {

    public String mapType(ColumnType columnType) {
        switch (columnType) {
            case BIGINT, BIGSERIAL -> {
                return "Long";
            }
            case BOOLEAN -> {
                return "Boolean";
            }
            case INT -> {
                return "Integer";
            }
            case TEXT -> {
                return "String";
            }
            case NUMERIC -> {
                return "java.math.BigDecimal";
            }
            case DATE -> {
                return "java.time.LocalDate";
            }
            case TIMESTAMP -> {
                return "java.time.LocalDateTime";
            }
            default -> throw new UnsupportedOperationException();
        }
    }
}
