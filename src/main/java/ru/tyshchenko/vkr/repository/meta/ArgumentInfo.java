package ru.tyshchenko.vkr.repository.meta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tyshchenko.vkr.entity.types.ColumnType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArgumentInfo {
    private ColumnType type;
    private String name;
}