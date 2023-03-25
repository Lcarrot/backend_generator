package ru.tyshchenko.vkr.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tyshchenko.vkr.entity.types.ColumnType;
import ru.tyshchenko.vkr.entity.types.ConstraintType;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Column {

    private ColumnType type;
    private String name;
    private Set<ConstraintType> constraintTypes;
}
