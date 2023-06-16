package ru.tyshchenko.vkr.engine.api.models.repository.meta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tyshchenko.vkr.engine.api.models.entity.types.ColumnType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArgumentInfo {
    private ColumnType type;
    private String name;
}